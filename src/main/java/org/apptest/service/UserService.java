package org.apptest.service;

import java.util.UUID;

import org.apptest.entity.User;
import org.apptest.param.LoginParam;
import org.apptest.param.RegisterParam;
import org.apptest.repository.LoginRepository;
import org.apptest.repository.UserRepository;
import org.apptest.response.LoginResponse;
import org.apptest.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    Mailer mailer;

    @Transactional
    public void register(RegisterParam request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.id = UUID.randomUUID();
        // user.name = request.name;
        // user.username = request.username;
        user.email = request.email;
        user.passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt());
        user.status = "UNVERIFIED";
        user.verificationToken = UUID.randomUUID().toString(); // Token verifikasi

        userRepository.save(user);
        String verificationLink = "http://localhost:8081/auth/verify?token=" + user.verificationToken;

       String htmlBody = """
        <html>
          <body style="font-family: Arial, sans-serif; line-height: 1.6;">
            <h2 style="color: #2c3e50;">Verifikasi Akun Anda</h2>
            <p>Halo,${user.username}</p>
            <p>Terima kasih telah mendaftar. Silakan klik tombol di bawah ini untuk memverifikasi akun Anda:</p>
            <p>
              <a href="%s" style="background-color: #3498db; color: white; padding: 10px 20px;
               text-decoration: none; border-radius: 5px;">Verifikasi Akun</a>
            </p>
            <p>Jika tombol tidak berfungsi, salin dan tempel tautan berikut di browser Anda:</p>
            <p><a href="%s">%s</a></p>
            <p>Terima kasih,<br>Tim Support</p>
          </body>
        </html>
        """.formatted(verificationLink, verificationLink, verificationLink);

    mailer.send(Mail.withHtml(
        user.email,
        "Verifikasi Akun",
        htmlBody
    ));
        // TODO: Kirim email verifikasi (akan dibuat setelah ini)
    }

    @Transactional
    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new WebApplicationException("Token tidak valid", 404));
        user.status = "ACTIVE";
        user.verificationToken = null; // hapus token setelah digunakan
        userRepository.save(user);
    }


     @Inject
    LoginRepository loginRepository;
@Transactional
public LoginResponse login(LoginParam request) {
    User user = userRepository.findByEmail(request.email)
            .orElseThrow(() -> new WebApplicationException("Email tidak ditemukan", 404));

    if (!"ACTIVE".equalsIgnoreCase(user.status)) {
        throw new WebApplicationException("Akun belum diverifikasi. Silakan verifikasi akun Anda.", 403);
    }

    if (!BCrypt.checkpw(request.password, user.passwordHash)) {
        throw new WebApplicationException("Email atau password salah", 401);
    }

    String accessToken = JwtUtil.generateAccessToken(user.id, user.email);
    String refreshToken = JwtUtil.generateRefreshToken(user.id);

    return new LoginResponse(user.id, user.email, "Login berhasil", accessToken, refreshToken);
}
    // @Transactional
//     @SuppressWarnings("empty-statement")
//     public LoginResponse login(LoginParam request){
//         User user = userRepository.findByEmail(request.email).orElseThrow(()->new WebApplicationException("Email Not Found",404));

//         if (!"ACTIVE".equalsIgnoreCase(user.status)) {
//             throw new WebApplicationException("Account not verified. Please check your email and verify your account first.", 403);
//         }
//       if (!BCrypt.checkpw(request.password, user.passwordHash)) {
//             throw new WebApplicationException("Invalid email or password", 401);
//         }

//         // Return successful login response
//         // return new LoginResponse(user.id, user.email, "Login successful");
//     }
}