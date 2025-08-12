package model;

public class Transaksi {
    private String tanggal;
    private String jenis;
    private String kategori;
    private double jumlah;
    private String catatan;

    public Transaksi(String tanggal, String jenis, String kategori, double jumlah, String catatan) {
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.catatan = catatan;
    }

    public String getTanggal() { return tanggal; }
    public String getJenis() { return jenis; }
    public String getKategori() { return kategori; }
    public double getJumlah() { return jumlah; }
    public String getCatatan() { return catatan; }

    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}