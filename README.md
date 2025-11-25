# Klp11_UasPrakPBO_A

![WhatsApp Image 2025-11-25 at 02 26 00](https://github.com/user-attachments/assets/0624fe6f-8c3f-4b1a-8852-07154756dc53)


# 🌸 Sistem Penjualan Bunga 

Sistem ini merupakan aplikasi penjualan bunga berbasis Java yang menggunakan pendekatan OOP dan antarmuka JavaFX sebagai media interaksi pengguna. Sistem memiliki dua peran utama yaitu Admin dan Customer, di mana seluruh pengguna wajib melakukan registrasi dan login sebelum mengakses sistem. Setelah login berhasil, setiap pengguna akan diarahkan ke menu sesuai perannya masing-masing agar dapat mengelola dan melakukan transaksi pembelian bunga secara efisien.

Pada sisi Admin, sistem menyediakan fungsi lengkap untuk mengatur seluruh data dan proses transaksi. Admin dapat mengelola barang secara penuh melalui fitur CRUD, termasuk melihat daftar barang serta menambah, mengedit, dan menghapus data bunga yang mencakup atribut nama, harga, stok, warna, jenis, tipe, dan file gambar produk. Admin juga memperoleh kemampuan untuk mengelola diskon atau voucher, seperti menampilkan daftar diskon, menambahkan diskon baru, mengaktifkan atau menonaktifkan diskon, dan menghapus data diskon. Selain itu, Admin dapat memantau transaksi yang dilakukan oleh pelanggan, melihat total transaksi, waktu, dan status pembayaran, menerima transaksi, serta mengubah status menjadi selesai. Seluruh fungsi tersebut dikemas dalam tampilan dashboard admin, yang memanfaatkan komponen antarmuka JavaFX seperti form input data, dialog pop-up untuk proses editing, TableView untuk seleksi item, serta tombol refresh manual untuk memperbarui informasi tampilan. Admin juga dapat melakukan logout untuk kembali ke halaman login.

Sedangkan pada sisi Customer, sistem menyediakan pengalaman belanja yang realistis melalui fitur keranjang belanja, katalog produk, pembayaran, dan riwayat transaksi. Customer dapat melihat dan mengelola isi keranjang dengan bebas, termasuk menambah bunga ke keranjang dengan menentukan jumlah pembelian, menghapus barang, melihat subtotal setiap barang, hingga menghitung total seluruh isi keranjang. Pada halaman katalog, Customer dapat menjelajahi seluruh produk bunga dalam tampilan card berisi gambar produk, nama barang, harga, dan stok, serta melakukan penyaringan produk berdasarkan jenis, tipe, atau kedua kriteria tersebut. Ketika melakukan checkout, Customer dapat memilih antara pembayaran COD, Bank Transfer, atau QRIS, sekaligus memasukkan kode voucher untuk mendapatkan diskon. Sistem akan menghitung total pembayaran secara otomatis berdasarkan diskon yang valid dan mengurangi stok barang setelah transaksi berhasil. Untuk memberikan pengalaman pengguna yang lengkap, Customer juga dapat melihat riwayat transaksi secara rinci, mulai dari ID transaksi, tanggal transaksi, total akhir pembelian, hingga status seperti “Menunggu” atau “Diterima Admin” dengan tampilan warna yang ditentukan melalui CSS. Setiap transaksi dapat dibuka dalam bentuk invoice dialog yang bersifat read-only, menggunakan font monospace agar daftar pembelian lebih rapi dan mudah dibaca. Customer juga dapat melihat informasi akun seperti username dan nama lengkap serta melakukan logout untuk kembali ke halaman login.

Secara keseluruhan, aplikasi ini dirancang untuk mensimulasikan proses jual beli bunga secara digital dengan alur interaksi yang lengkap mulai dari katalog hingga pembuatan invoice. Sistem Java ini tidak hanya memanfaatkan konsep OOP seperti inheritance, encapsulation, dan polymorphism, tetapi juga mengintegrasikan JavaFX sebagai antarmuka yang responsif dan mudah digunakan. Dengan adanya pembagian peran Admin dan Customer, sistem mampu menampilkan pengalaman operasional marketplace sederhana sekaligus memperlihatkan implementasi nyata dari desain perangkat lunak berbasis objek.


**# Hasil Output GUI**

Tampilan Login:

<img width="422" height="601" alt="image" src="https://github.com/user-attachments/assets/184d3f8e-8c93-44bb-b25c-4223456b3be7" />



Tampilan Customer:
<img width="1300" height="494" alt="image" src="https://github.com/user-attachments/assets/5d36627b-2f9b-453f-928f-830a6fc880e4" />


Tampilan Admin:
<img width="1300" height="511" alt="image" src="https://github.com/user-attachments/assets/b8af5f84-9a9d-45ae-a2ca-a735b65bd2b5" />

