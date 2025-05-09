package org.apptest.resource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apptest.entity.Buku;
import org.apptest.param.BukuParam;
import org.apptest.response.ApiResponse;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/buku")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BukuResource {
    @Inject
    EntityManager em;

    // Get All data Buku
    @GET
    @Path("/all")
    public Response getAll() {
        List<Buku> list = em.createQuery("FROM Buku", Buku.class).getResultList();
        return Response.ok(new ApiResponse<>(true, "Data buku ditemukan", list)).build();
    }

 @GET
@Path("/all/procedure")
@Transactional  // PENTING: Anotasi ini dari javax.transaction atau jakarta.transaction
public Response getAllByProcedure() {
    // 1. Eksekusi prosedur (wajib dalam transaction)
    em.createNativeQuery("CALL ambil_semua_buku_proc()").executeUpdate();

    // 2. Ambil data dari tabel sementara
    List<Object[]> resultList = em
        .createNativeQuery("SELECT * FROM temp_buku")
        .getResultList();

    List<Buku> list = new ArrayList<>();
    for (Object[] obj : resultList) {
        Buku buku = new Buku();
        buku.setId(((Number) obj[0]).longValue());
        buku.setJudul((String) obj[1]);
        buku.setPenulis((String) obj[2]);
        buku.setPenerbit((String) obj[3]);
        buku.setTanggal_terbit((Date) obj[4]);
        list.add(buku);
    }

    return Response.ok(new ApiResponse<>(true, "Data buku dari prosedur berhasil", list)).build();
}


    // Get buku by ID menggunakan Entity
    @GET
    @Path("/detail/entity/{id}")
    public Response getByIdEntity(@PathParam("id") Long id) {
        Buku buku = em.find(Buku.class, id);
        if (buku == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse<>(false, "Data buku tidak ditemukan", null)).build();
        }
        return Response.ok(new ApiResponse<>(true, "Data buku ditemukan", buku)).build();
    }
    // Get buku by ID menggunakan Procedure
@GET
@Path("/detail/procedure/{id}")
public Response getByIdProcedure(@PathParam("id") Long id) {
    // Menjalankan prosedur ambil_buku_proc dan mendapatkan output parameter
    StoredProcedureQuery query = em.createStoredProcedureQuery("ambil_buku_proc");

    // Daftarkan parameter input dan output
    query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN); // Nama parameter input sesuai dengan prosedur
    query.registerStoredProcedureParameter("p_id_buku", Long.class, ParameterMode.OUT); // Sesuaikan nama output parameter
    query.registerStoredProcedureParameter("p_judul", String.class, ParameterMode.OUT); // Sesuaikan nama output parameter
    query.registerStoredProcedureParameter("p_penulis", String.class, ParameterMode.OUT); // Sesuaikan nama output parameter
    query.registerStoredProcedureParameter("p_penerbit", String.class, ParameterMode.OUT); // Sesuaikan nama output parameter
    query.registerStoredProcedureParameter("p_tanggal_terbit", Date.class, ParameterMode.OUT); // Sesuaikan nama output parameter

    // Set parameter input (id)
    query.setParameter("p_id", id); // Set sesuai nama input parameter

    // Eksekusi query untuk memanggil prosedur
    query.execute();

    // Mengambil nilai output parameter sesuai dengan nama yang ada di prosedur
    Long idBuku = (Long) query.getOutputParameterValue("p_id_buku"); // Sesuaikan nama output parameter
    String judul = (String) query.getOutputParameterValue("p_judul"); // Sesuaikan nama output parameter
    String penulis = (String) query.getOutputParameterValue("p_penulis"); // Sesuaikan nama output parameter
    String penerbit = (String) query.getOutputParameterValue("p_penerbit"); // Sesuaikan nama output parameter
    Date tanggal_terbit = (Date) query.getOutputParameterValue("p_tanggal_terbit"); // Sesuaikan nama output parameter

    if (judul == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiResponse<>(false, "Buku tidak ditemukan", null))
                .build();
    }

    // Membuat objek Buku dan mengisi data dari output parameter
    Buku buku = new Buku();
    buku.setId(idBuku); // Pastikan Anda memiliki setter untuk idBuku
    buku.setJudul(judul); // Pastikan Anda memiliki setter untuk judul
    buku.setPenulis(penulis); // Pastikan Anda memiliki setter untuk penulis
    buku.setPenerbit(penerbit); // Pastikan Anda memiliki setter untuk penerbit
    buku.setTanggal_terbit(tanggal_terbit); // Pastikan Anda memiliki setter untuk tanggalTerbit

    return Response.ok(new ApiResponse<>(true, "Buku ditemukan", buku)).build();
}




    
  @POST
@Path("/create/persist")
@Transactional
public Response create(BukuParam param) {
    Buku buku = new Buku();
    buku.setJudul(param.judul);
    buku.setPenulis(param.penulis);
    buku.setPenerbit(param.penerbit);
    buku.setTanggal_terbit(param.tanggal_terbit);
    em.persist(buku);
    return Response.ok(new ApiResponse<>(true, "Buku berhasil ditambahkan", buku)).build();
}



    @POST
    @Path("/create/procedure")
    @Transactional
    public Response tambahBuku(BukuParam param) {
        em.createNativeQuery("CALL tambah_buku(:judul, :penulis, :penerbit, :tanggal_terbit)")
            .setParameter("judul", param.judul)
            .setParameter("penulis", param.penulis)
            .setParameter("penerbit", param.penerbit)
            .setParameter("tanggal_terbit", param.tanggal_terbit)
            .executeUpdate();
        return Response.ok().entity(Map.of(
            "status", true,
            "message", "Buku berhasil ditambahkan"
        )).build();
    }

@PUT
@Path("/update/entity/{id}")
@Transactional
public Response updateDirectly(@PathParam("id") Long id, BukuParam param) {
    // Mencari buku berdasarkan ID
    Buku buku = em.find(Buku.class, id);
    if (buku == null) {
        // Jika buku tidak ditemukan, kembalikan respons NOT_FOUND
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiResponse<>(false, "Data buku tidak ditemukan", null)).build();
    }

    // Memperbarui properti buku dengan data yang diterima
    buku.setJudul(param.judul);
    buku.setPenulis(param.penulis);
    buku.setPenerbit(param.penerbit);
    buku.setTanggal_terbit(param.tanggal_terbit);

    // Secara otomatis JPA akan mengelola perubahan ini dan menyimpannya setelah transaksi selesai

    // Mengembalikan respons sukses dengan data buku yang diperbarui
    return Response.ok(new ApiResponse<>(true, "Buku berhasil diperbarui", buku)).build();
}



    @PUT
@Path("/update/procedure/{id}")
@Transactional
public Response updateWithProcedure(@PathParam("id") Long id, BukuParam param) {
    // Memanggil prosedur ubah_buku untuk memperbarui data buku
    try {
        // Memanggil prosedur ubah_buku di database
        em.createNativeQuery("CALL ubah_buku(:p_id, :p_judul, :p_penulis, :p_penerbit, :p_tanggal_terbit)")
            .setParameter("p_id", id)
            .setParameter("p_judul", param.judul)
            .setParameter("p_penulis", param.penulis)
            .setParameter("p_penerbit", param.penerbit)
            .setParameter("p_tanggal_terbit", param.tanggal_terbit)
            .executeUpdate();

        // Mengambil buku yang telah diperbarui
        Buku buku = em.find(Buku.class, id);
        if (buku == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse<>(false, "Data buku tidak ditemukan", null)).build();
        }

        // Kembalikan respons sukses
        return Response.ok(new ApiResponse<>(true, "Buku berhasil diperbarui", buku)).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponse<>(false, "Gagal memperbarui buku", null)).build();
    }
}


    @DELETE
    @Path("/delete/entity/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Buku buku = em.find(Buku.class, id);
        if (buku == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse<>(false, "Data buku tidak ditemukan", null)).build();
        }

        em.remove(buku);
        return Response.ok(new ApiResponse<>(true, "Buku berhasil dihapus", null)).build();
    }


@DELETE
@Path("/delete/procedure/{id}")
@Transactional
public Response deleteWithProcedure(@PathParam("id") Long id) {
    try {
        em.createNativeQuery("CALL hapus_buku(:id)")
            .setParameter("id", id)
            .executeUpdate();

        return Response.ok(new ApiResponse<>(true, "Buku berhasil dihapus dengan prosedur", null)).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ApiResponse<>(false, "Gagal menghapus buku: " + e.getMessage(), null)).build();
    }
}
}
