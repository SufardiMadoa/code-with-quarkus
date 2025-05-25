package org.apptest.param;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BukuParam {

    @NotBlank(message = "Judul tidak boleh kosong")
    public String judul;

    @NotBlank(message = "Penulis tidak boleh kosong")
    public String penulis;

    @NotBlank(message = "Penerbit tidak boleh kosong")
    public String penerbit;

    @NotNull(message = "Tanggal terbit tidak boleh kosong")
    public Date tanggal_terbit;

}
