import csv

class Node:
    def __init__(self, id_key, barang):
        self.id = id_key
        self.barang = barang
        self.left = self.right = None

class BST:
    def __init__(self):
        self.root = None

    # 1. Tambah Data
    def insert(self, id_key, barang):
        if not self.root:
            self.root = Node(id_key, barang)
        else:
            self._insert(self.root, id_key, barang)

    def _insert(self, node, id_key, barang):
        if id_key < node.id:
            if not node.left: node.left = Node(id_key, barang)
            else: self._insert(node.left, id_key, barang)
        elif id_key > node.id:
            if not node.right: node.right = Node(id_key, barang)
            else: self._insert(node.right, id_key, barang)

    # 2. Load CSV
    def load_csv(self, file_name):
        try:
            with open(file_name, mode='r', encoding='utf-8') as file:
                reader = csv.reader(file, delimiter=';')
                count = 0
                for row in reader:
                    if not row or len(row) < 2: continue
                    try:
                        id_val = int(row[0].strip())
                        nama_val = row[1].strip()
                        self.insert(id_val, nama_val)
                        count += 1
                    except ValueError: continue
                print(f"\n>>> Berhasil memuat {count} data!")
        except FileNotFoundError:
            print("\n[!] File tidak ditemukan!")

    # 3. Cari Data
    def search(self, node, id_key):
        if not node or node.id == id_key: return node
        if id_key < node.id: return self.search(node.left, id_key)
        return self.search(node.right, id_key)

    # 4. Hapus Data
    def delete(self, node, id_key):
        if not node: return node
        if id_key < node.id:
            node.left = self.delete(node.left, id_key)
        elif id_key > node.id:
            node.right = self.delete(node.right, id_key)
        else:
            if not node.left: return node.right
            if not node.right: return node.left
            temp = self._min(node.right)
            node.id, node.barang = temp.id, temp.barang
            node.right = self.delete(node.right, temp.id)
        return node

    def _min(self, node):
        curr = node
        while curr.left: curr = curr.left
        return curr

    # 5. Fungsi Traversal Format Tabel
    def print_header(self, judul):
        print(f"\n--- LAPORAN {judul.upper()} ---")
        print("-" * 45)
        print(f"| {'ID':<10} | {'NAMA BARANG':<25} |")
        print("-" * 45)

    def traverse_tabel(self, node, mode):
        if node:
            if mode == 'pre': 
                print(f"| {node.id:<10} | {node.barang:<25} |")
            
            self.traverse_tabel(node.left, mode)
            
            if mode == 'in': 
                print(f"| {node.id:<10} | {node.barang:<25} |")
            
            self.traverse_tabel(node.right, mode)
            
            if mode == 'post': 
                print(f"| {node.id:<10} | {node.barang:<25} |")

# --- Main Program ---
bst = BST()

while True:
    print("\n" + "="*35)
    print("      SISTEM GUDANG BST")
    print("="*35)
    print("1. Load Data dari CSV")
    print("2. Tambah Data Manual")
    print("3. Cari Barang (by ID)")
    print("4. Hapus Barang")
    print("5. Lihat Laporan (Traversal)")
    print("6. Keluar")
    
    p = input("Pilih menu: ")

    if p == '1':
        f_name = input("Masukkan nama file CSV (misal: data100.csv): ")
        bst.load_csv(f_name)

    elif p == '2':
        while True:
            try:
                id_in = int(input("\nMasukkan ID (Angka): "))
                brg_in = input("Masukkan Nama Barang: ")
                bst.insert(id_in, brg_in)
                if input("Tambah lagi? (y/t): ").lower() != 'y': break
            except ValueError:
                print("ID harus angka!")

    elif p == '3':
        try:
            target = int(input("Cari ID: "))
            res = bst.search(bst.root, target)
            print(f"Ketemu! Nama Barang: {res.barang}" if res else "ID tidak ada.")
        except: print("Input harus angka!")

    elif p == '4':
        try:
            target = int(input("Hapus ID: "))
            bst.root = bst.delete(bst.root, target)
            print("Proses hapus selesai.")
        except: print("Input harus angka!")

    elif p == '5':
        if not bst.root:
            print("[!] Gudang kosong.")
        else:
            print("\nPilih Jenis Traversal:")
            print("a. Inorder   (Terurut ID)")
            print("b. Preorder  (Akar Duluan)")
            print("c. Postorder (Daun Duluan)")
            sub_p = input("Pilih (a/b/c): ").lower()
            
            if sub_p == 'a':
                bst.print_header("Inorder")
                bst.traverse_tabel(bst.root, 'in')
                print("-" * 45)
            elif sub_p == 'b':
                bst.print_header("Preorder")
                bst.traverse_tabel(bst.root, 'pre')
                print("-" * 45)
            elif sub_p == 'c':
                bst.print_header("Postorder")
                bst.traverse_tabel(bst.root, 'post')
                print("-" * 45)
            else:
                print("Pilihan tidak valid.")

    elif p == '6':
        print("Selesai!")
        break