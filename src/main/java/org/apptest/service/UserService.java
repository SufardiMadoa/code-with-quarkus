package org.apptest.service;

import java.util.UUID;

import org.apptest.entity.User;
import org.apptest.param.LoginParam;
import org.apptest.param.RegisterParam;
import org.apptest.repository.UserRepository;
import org.apptest.response.LoginResponse;
import org.apptest.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UserService {

    @Inject
    EntityManager entityManager;

    @Inject
    UserRepository userRepository;

    @Inject
    Mailer mailer;

    @Transactional
    public String register(RegisterParam param) {
        try {
            // Validasi input
            validateRegisterParam(param);
            
            // Validasi password
          
            
            // Cek email sudah ada
            long emailCount = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", param.email)
                .getSingleResult();
            
            if (emailCount > 0) {
                return "Email sudah terdaftar";
            }
            
            // Cek username sudah ada
            long usernameCount = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", param.username)
                .getSingleResult();
            // Buat user baru
            User user = new User();
            user.setName(param.name);
            user.setUsername(param.username);
            user.setEmail(param.email);
            // Perbaikan: Gunakan BCrypt secara konsisten
            user.setPasswordHash(BCrypt.hashpw(param.password, BCrypt.gensalt()));
            user.setVerificationToken(UUID.randomUUID().toString());
            user.setStatus("PENDING"); // Set status awal
            
            // Simpan ke database
            entityManager.persist(user);
            
mailer.send(
    Mail.withText(
        param.email,
        "Halo" + user.getName(),
        "Terima kasih telah mendaftar. Klik link berikut untuk verifikasi: " +
        "https://code-with-quarkus-8.onrender.com/auth/verify?token=" + user.getVerificationToken()
    )
);
        return "Registrasi berhasil";    
        } catch (Exception e) {
            return "Registrasi gagal: " + e.getMessage();
        }
    }
    
    private void validateRegisterParam(RegisterParam param) {
        if (param.email == null || param.email.isEmpty()) {
            throw new IllegalArgumentException("Email tidak boleh kosong");
        }
        if (param.username == null || param.username.isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (param.password == null || param.password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
        // Tambahkan validasi email format jika diperlukan
    }
    
    public User findByEmail(String email) {
        try {
            return entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    public boolean verifyEmail(String token) {
        try {
            User user = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.verificationToken = :token", User.class)
                .setParameter("token", token)
                .getSingleResult();
            
            // Perbaikan: Gunakan status yang konsisten dengan login
            user.setStatus("ACTIVE");
            user.setVerificationToken(null);
            entityManager.merge(user);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public LoginResponse login(LoginParam request) {
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new WebApplicationException("Email tidak ditemukan", 404));

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new WebApplicationException("Akun belum diverifikasi. Silakan verifikasi akun Anda.", 403);
        }

        if (!BCrypt.checkpw(request.password, user.getPasswordHash())) {
            throw new WebApplicationException("Email atau password salah", 401);
        }

        String accessToken = JwtUtil.generateAccessToken(user.id, user.getEmail());
        String refreshToken = JwtUtil.generateRefreshToken(user.id);

        return new LoginResponse(user.id, user.getEmail(), "Login berhasil", accessToken, refreshToken);
    }
}