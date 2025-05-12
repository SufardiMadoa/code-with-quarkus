package org.apptest.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class BukuParam {

    @NotBlank(message = "Judul tidak boleh kosong")
    public String judul;

    @NotBlank(message = "Penulis tidak boleh kosong")
    public String penulis;

    @NotBlank(message = "Penerbit tidak boleh kosong")
    public String penerbit;

    @NotNull(message = "Tanggal terbit tidak boleh kosong")
    public Date tanggal_terbit;

    @NotNull(message = "ID jenis tidak boleh kosong")
    public Long id_jenis;
}
