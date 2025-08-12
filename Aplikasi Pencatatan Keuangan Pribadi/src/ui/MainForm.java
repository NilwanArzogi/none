package src.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.model.Transaksi;

public class MainForm extends JFrame {
    private JSpinner spTanggal;
    private JTextField tfKategori, tfJumlah, tfCatatan;
    private JComboBox<String> cbJenis;
    private JTable table;
    private DefaultTableModel tableModel;
    private java.util.List<Transaksi> transaksiList = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#,###");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JLabel lblTotalPemasukan = new JLabel("Total Pemasukan: Rp 0");
    private JLabel lblTotalPengeluaran = new JLabel("Total Pengeluaran: Rp 0");
    private JLabel lblSaldo = new JLabel("Saldo: Rp 0");
    private File dataFile = new File("data_keuangan.csv");

    public MainForm() {
        setTitle("Aplikasi Keuangan Pribadi");
        
        initComponents();
        
        loadDummyData();
        
        updateTable();

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
        loadData();
    }

    private void loadDummyData() {
        transaksiList.clear();
        
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = createFormPanel();
        
        JPanel buttonPanel = createButtonPanel();
        
        JPanel tablePanel = createTablePanel();
        
        JPanel summaryPanel = createSummaryPanel();
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Input Transaksi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spTanggal = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spTanggal, "yyyy-MM-dd");
        spTanggal.setEditor(dateEditor);
        spTanggal.setPreferredSize(new Dimension(150, 25));
        
        spTanggal.setValue(new Date());
        
        tfKategori = new JTextField(15);
        tfJumlah = new JTextField(15);
        tfCatatan = new JTextField(15);
        cbJenis = new JComboBox<>(new String[]{"Pemasukan", "Pengeluaran"});
        
        cbJenis.setPreferredSize(new Dimension(150, 25));
        cbJenis.setBackground(Color.WHITE);
        cbJenis.setForeground(Color.BLACK);
        cbJenis.setFont(new Font("Arial", Font.PLAIN, 12));
        
        cbJenis.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                
                button.setBackground(new Color(240, 240, 240)); 
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(192, 192, 192)), 
                    BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ));
                button.setFocusPainted(false);
                button.setOpaque(true);
                
                button.setText("▼");
                button.setFont(new Font("Arial", Font.PLAIN,8));
                button.setForeground(Color.BLACK);
                
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(new Color(225, 225, 225));
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(new Color(240, 240, 240)); 
                    }
                });
                
                return button;
            }
            
            @Override
            protected void configureEditor() {
                super.configureEditor();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                    ((JTextField) editor).setBackground(Color.WHITE);
                }
            }
            
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                c.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192)));
                c.setBackground(Color.WHITE);
            }
        });
        
        cbJenis.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192)));
        
        cbJenis.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(new Color(70, 130, 180));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                setFont(new Font("Arial", Font.PLAIN, 12));
                setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tanggal :"), gbc);
        gbc.gridx = 1;
        panel.add(spTanggal, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Jenis :"), gbc);
        gbc.gridx = 3;
        panel.add(cbJenis, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Kategori :"), gbc);
        gbc.gridx = 1;
        panel.add(tfKategori, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Jumlah :"), gbc);
        gbc.gridx = 3;
        panel.add(tfJumlah, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Catatan :"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(tfCatatan, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder(""));

        JButton btnTambah = new JButton("Tambah Transaksi");
        JButton btnEdit = new JButton("Edit Transaksi");
        JButton btnHapus = new JButton("Hapus Transaksi");
        JButton btnExport = new JButton("Export File");
        JButton btnImport = new JButton("Import File");
        JButton btnClear = new JButton("Clear Form");

        Dimension buttonSize = new Dimension(140, 35);
        btnTambah.setPreferredSize(buttonSize);
        btnEdit.setPreferredSize(buttonSize);
        btnHapus.setPreferredSize(buttonSize);
        btnExport.setPreferredSize(buttonSize);
        btnImport.setPreferredSize(buttonSize);
        btnClear.setPreferredSize(buttonSize);

        Color blueColor = new Color(70, 130, 180);
        Color hoverColor = new Color(100, 149, 237); 
        
        JButton[] buttons = {btnTambah, btnEdit, btnHapus, btnExport, btnImport, btnClear};
        
        for (JButton btn : buttons) {
            btn.setBackground(blueColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(blueColor);
                }
            });
        }

        panel.add(btnTambah);
        panel.add(btnEdit);
        panel.add(btnHapus);
        panel.add(btnClear);
        panel.add(btnExport);
        panel.add(btnImport);

        setupButtonListeners(btnTambah, btnEdit, btnHapus, btnExport, btnImport, btnClear);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi"));

        String[] columnNames = {"Tanggal", "Jenis", "Kategori", "Jumlah", "Catatan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(25);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        panel.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    Transaksi t = transaksiList.get(row);
                    try {
                        Date date = dateFormat.parse(t.getTanggal());
                        spTanggal.setValue(date);
                    } catch (Exception ex) {
                        spTanggal.setValue(new Date());
                    }
                    cbJenis.setSelectedItem(t.getJenis());
                    tfKategori.setText(t.getKategori());
                    tfJumlah.setText(String.valueOf((int)t.getJumlah()));
                    tfCatatan.setText(t.getCatatan());
                }
            }
        });

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Ringkasan Keuangan"));
        panel.setPreferredSize(new Dimension(800, 80));

        lblTotalPemasukan.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalPengeluaran.setHorizontalAlignment(SwingConstants.CENTER);
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);

        lblTotalPemasukan.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalPengeluaran.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));

        lblTotalPemasukan.setForeground(Color.GREEN.darker());
        lblTotalPengeluaran.setForeground(Color.RED.darker());
        lblSaldo.setForeground(Color.BLUE.darker());

        panel.add(lblTotalPemasukan);
        panel.add(lblTotalPengeluaran);
        panel.add(lblSaldo);

        return panel;
    }

    private void setupButtonListeners(JButton btnTambah, JButton btnEdit, JButton btnHapus, JButton btnExport, JButton btnImport, JButton btnClear) {
        
        btnTambah.addActionListener(e -> {
            if (validateInput()) {
                try {
                    Date selectedDate = (Date) spTanggal.getValue();
                    String tanggal = dateFormat.format(selectedDate);
                    String jenis = cbJenis.getSelectedItem().toString();
                    String kategori = tfKategori.getText().trim();
                    double jumlah = Double.parseDouble(tfJumlah.getText().replace(".", "").replace(",", ""));
                    String catatan = tfCatatan.getText().trim();

                    Transaksi t = new Transaksi(tanggal, jenis, kategori, jumlah, catatan);
                    transaksiList.add(t);
                    updateTable();
                    saveData();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                if (validateInput()) {
                    try {
                        Transaksi t = transaksiList.get(row);
                        Date selectedDate = (Date) spTanggal.getValue();
                        t.setTanggal(dateFormat.format(selectedDate));
                        t.setJenis(cbJenis.getSelectedItem().toString());
                        t.setKategori(tfKategori.getText().trim());
                        t.setJumlah(Double.parseDouble(tfJumlah.getText().replace(".", "").replace(",", "")));
                        t.setCatatan(tfCatatan.getText().trim());
                        updateTable();
                        saveData();
                        clearForm();
                        JOptionPane.showMessageDialog(this, "Transaksi berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin menghapus transaksi ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    transaksiList.remove(row);
                    updateTable();
                    saveData();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> clearForm());

        btnExport.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Simpan File");
            fc.setSelectedFile(new File("data_keuangan.csv"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                    fw.write("Tanggal,Jenis,Kategori,Jumlah,Catatan\n");
                    for (Transaksi t : transaksiList) {
                        fw.write(String.format("%s,%s,%s,%.0f,%s\n",
                            t.getTanggal(), t.getJenis(), t.getKategori(),
                            t.getJumlah(), t.getCatatan().replace(",", ";")));
                    }
                    JOptionPane.showMessageDialog(this, "Data berhasil diekspor!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnImport.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Pilih File");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner scanner = new Scanner(fc.getSelectedFile())) {
                    transaksiList.clear();
                    if (scanner.hasNextLine()) {
                        scanner.nextLine(); 
                    }
                    while (scanner.hasNextLine()) {
                        String[] data = scanner.nextLine().split(",", -1);
                        if (data.length >= 5) {
                            transaksiList.add(new Transaksi(data[0], data[1], data[2],
                                    Double.parseDouble(data[3]), data[4]));
                        }
                    }
                    updateTable();
                    saveData();
                    JOptionPane.showMessageDialog(this, "Data berhasil diimpor!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengimpor file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean validateInput() {
        if (spTanggal.getValue() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            spTanggal.requestFocus();
            return false;
        }
        if (tfKategori.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kategori tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            tfKategori.requestFocus();
            return false;
        }
        if (tfJumlah.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            tfJumlah.requestFocus();
            return false;
        }
        try {
            Double.parseDouble(tfJumlah.getText().replace(".", "").replace(",", ""));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            tfJumlah.requestFocus();
            return false;
        }
        return true;
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        double totalIn = 0, totalOut = 0;
        for (Transaksi t : transaksiList) {
            tableModel.addRow(new Object[]{
                    t.getTanggal(),
                    t.getJenis(),
                    t.getKategori(),
                    "Rp " + df.format(t.getJumlah()),
                    t.getCatatan()
            });
            if (t.getJenis().equalsIgnoreCase("Pemasukan")) {
                totalIn += t.getJumlah();
            } else if (t.getJenis().equalsIgnoreCase("Pengeluaran")) {
                totalOut += t.getJumlah();
            }
        }
        double saldo = totalIn - totalOut;
        lblTotalPemasukan.setText("Total Pemasukan: Rp " + df.format(totalIn));
        lblTotalPengeluaran.setText("Total Pengeluaran: Rp " + df.format(totalOut));
        lblSaldo.setText("Saldo: Rp " + df.format(saldo));
    }

    private void clearForm() {
        spTanggal.setValue(new Date());
        tfKategori.setText("");
        tfJumlah.setText("");
        tfCatatan.setText("");
        cbJenis.setSelectedIndex(0);
        table.clearSelection();
    }

    private void saveData() {
        try (FileWriter fw = new FileWriter(dataFile)) {
            fw.write("Tanggal,Jenis,Kategori,Jumlah,Catatan\n");
            for (Transaksi t : transaksiList) {
                fw.write(String.format("%s,%s,%s,%.0f,%s\n",
                    t.getTanggal(), t.getJenis(), t.getKategori(),
                    t.getJumlah(), t.getCatatan().replace(",", ";")));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        if (dataFile.exists()) {
            try (Scanner scanner = new Scanner(dataFile)) {
                transaksiList.clear();
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); 
                }
                while (scanner.hasNextLine()) {
                    String[] data = scanner.nextLine().split(",", -1);
                    if (data.length >= 5) {
                        transaksiList.add(new Transaksi(data[0], data[1], data[2],
                                Double.parseDouble(data[3]), data[4]));
                    }
                }
                updateTable();
            } catch (Exception ex) {
                System.out.println("Gagal memuat data, menggunakan dummy data");
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}