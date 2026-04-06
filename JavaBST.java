import java.io.*;
import java.util.Scanner;

class Node {
    int id;
    String barang;
    Node left, right;

    public Node(int id, String barang) {
        this.id = id;
        this.barang = barang;
        left = right = null;
    }
}

public class JavaBST {
    Node root;
    Scanner sc = new Scanner(System.in);

    // 1. Tambah Data
    void insert(int id, String barang) { root = insertRec(root, id, barang); }
    Node insertRec(Node root, int id, String barang) {
        if (root == null) return new Node(id, barang);
        if (id < root.id) root.left = insertRec(root.left, id, barang);
        else if (id > root.id) root.right = insertRec(root.right, id, barang);
        return root;
    }

    // 2. Load CSV (Menggunakan pemisah Titik Koma ';')
    void loadCSV(String fileName) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 2) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String nama = data[1].trim();
                        insert(id, nama);
                        count++;
                    } catch (NumberFormatException e) {
                        continue; // Lewati jika baris pertama judul
                    }
                }
            }
            System.out.println("\n>>> Berhasil memuat " + count + " data!");
        } catch (IOException e) {
            System.out.println("\n[!] Error: File tidak ditemukan!");
        }
    }

    // 3. Cari Data
    Node search(Node root, int id) {
        if (root == null || root.id == id) return root;
        return (id < root.id) ? search(root.left, id) : search(root.right, id);
    }

    // 4. Hapus Data
    Node delete(Node root, int id) {
        if (root == null) return root;
        if (id < root.id) root.left = delete(root.left, id);
        else if (id > root.id) root.right = delete(root.right, id);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            Node temp = minValue(root.right);
            root.id = temp.id;
            root.barang = temp.barang;
            root.right = delete(root.right, temp.id);
        }
        return root;
    }

    Node minValue(Node root) {
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr;
    }

    // 5. Fungsi Tabel & Traversal
    void printHeader(String judul) {
        System.out.println("\n--- LAPORAN " + judul.toUpperCase() + " ---");
        System.out.println("---------------------------------------------");
        System.out.printf("| %-10s | %-25s |\n", "ID", "NAMA BARANG");
        System.out.println("---------------------------------------------");
    }

    void traverseTabel(Node node, String mode) {
        if (node != null) {
            if (mode.equals("pre")) System.out.printf("| %-10d | %-25s |\n", node.id, node.barang);
            traverseTabel(node.left, mode);
            if (mode.equals("in")) System.out.printf("| %-10d | %-25s |\n", node.id, node.barang);
            traverseTabel(node.right, mode);
            if (mode.equals("post")) System.out.printf("| %-10d | %-25s |\n", node.id, node.barang);
        }
    }

    // --- Main Menu ---
    public void mainMenu() {
        while (true) {
            System.out.println("\n========= SISTEM GUDANG BST =========");
            System.out.println("1. Load Data dari CSV");
            System.out.println("2. Tambah Data Manual");
            System.out.println("3. Cari Barang (by ID)");
            System.out.println("4. Hapus Barang");
            System.out.println("5. Lihat Laporan (Traversal)");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");
            String pil = sc.next();

            if (pil.equals("6")) break;

            switch (pil) {
                case "1":
                    System.out.print("Nama file (misal data100.csv): ");
                    loadCSV(sc.next());
                    break;
                case "2":
                    char lagi;
                    do {
                        System.out.print("\nID (Angka): "); int id = sc.nextInt();
                        sc.nextLine(); // clear buffer
                        System.out.print("Nama Barang: "); String n = sc.nextLine();
                        insert(id, n);
                        System.out.print("Tambah lagi? (y/t): ");
                        lagi = sc.next().toLowerCase().charAt(0);
                    } while (lagi == 'y');
                    break;
                case "3":
                    System.out.print("Cari ID: ");
                    Node res = search(root, sc.nextInt());
                    System.out.println(res != null ? "Ketemu! Barang: " + res.barang : "ID tidak ada.");
                    break;
                case "4":
                    System.out.print("Hapus ID: ");
                    root = delete(root, sc.nextInt());
                    System.out.println("Proses selesai.");
                    break;
                case "5":
                    if (root == null) {
                        System.out.println("[!] Gudang kosong.");
                    } else {
                        System.out.println("\nPilih Traversal: (a) Inorder, (b) Preorder, (c) Postorder");
                        String sub = sc.next().toLowerCase();
                        if (sub.equals("a")) { printHeader("Inorder"); traverseTabel(root, "in"); }
                        else if (sub.equals("b")) { printHeader("Preorder"); traverseTabel(root, "pre"); }
                        else if (sub.equals("c")) { printHeader("Postorder"); traverseTabel(root, "post"); }
                        System.out.println("---------------------------------------------");
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new JavaBST().mainMenu();
    }
}