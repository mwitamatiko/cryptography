import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;
public class DesExample extends JFrame implements ActionListener {
    private JFrame window = new JFrame();
    private JPanel panel = new JPanel();
    private JLabel encrypt_label = new JLabel("encryption");
    private JLabel decrypt_label = new JLabel("decryption");
    private JLabel load_file1 = new JLabel("load file");
    private JLabel load_file2 = new JLabel("load file");
    private JTextField encrypt_text = new JTextField();
    private JTextField decrypt_text = new JTextField();
    private JButton btnBrowse1 = new JButton("browse");
    private JButton btnBrowse2 = new JButton("browse");
    private JButton btnEncrypt = new JButton("encrypt");
    private JButton btnDecrypt = new JButton("decrypt");
    private JButton btnSend = new JButton("send file");
    private JButton btnExit = new JButton("exit");
    Font font1 = new Font("quicksand", Font.BOLD, 16);
    JFileChooser fileChooser;

    String encrypt_key,decrypt_key;
    File selectedFile;

    public DesExample(){

        Scanner input = new Scanner(System.in);
        //window properties
        window.add(panel);
        window.setTitle("MAIN MENU");
        window.setSize(700,600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
        window.add(panel);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to exit?",
                        "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response==JOptionPane.YES_OPTION){
                    System.exit(0);}
                else{
                    window.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });

        panel.setLayout(null);

        encrypt_label.setBounds(250,30,200,40);
        encrypt_label.setFont(font1);
        panel.add(encrypt_label);

        load_file1.setBounds(10,100,200,40);
        load_file1.setFont(font1);
        panel.add(load_file1);

        encrypt_text.setBounds(100,100,450,40);
        encrypt_text.setFont(font1);
        encrypt_text.setEditable(false);
        panel.add(encrypt_text);

        btnBrowse1.setBounds(580,100,100,40);
        btnBrowse1.setFont(font1);
        btnBrowse1.addActionListener(this);
        panel.add(btnBrowse1);

        btnEncrypt.setBounds(100,150,450,40);
        btnEncrypt.setFont(font1);
        btnEncrypt.addActionListener(this);
        panel.add(btnEncrypt);

        decrypt_label.setBounds(250,250,200,40);
        decrypt_label.setFont(font1);
        panel.add(decrypt_label);

        load_file2.setBounds(10,300,200,40);
        load_file2.setFont(font1);
        panel.add(load_file2);

        decrypt_text.setBounds(100,300,450,40);
        decrypt_text.setEditable(false);
        decrypt_text.setFont(font1);
        panel.add(decrypt_text);

        btnBrowse2.setBounds(580,300,100,40);
        btnBrowse2.setFont(font1);
        btnBrowse2.addActionListener(this);
        panel.add(btnBrowse2);

        btnDecrypt.setBounds(100,350,450,40);
        btnDecrypt.setFont(font1);
        btnDecrypt.addActionListener(this);
        panel.add(btnDecrypt);

        btnSend.setBounds(100,450,200,40);
        btnSend.setFont(font1);
        btnSend.addActionListener(this);
        panel.add(btnSend);

        btnExit.setBounds(300,450,200,40);
        btnExit.setFont(font1);
        btnExit.addActionListener(this);
        panel.add(btnExit);

        fileChooser=new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    }
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e) { }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DesExample();
            }
        });
    }

    public static void encryptData(String encryptKey, InputStream inputStream, OutputStream outputStream) throws Throwable{
        cipherData(encryptKey, Cipher.ENCRYPT_MODE,inputStream,outputStream);
    }
    public static void decryptData(String decryptKey, InputStream inputStream, OutputStream outputStream) throws Throwable{
        cipherData(decryptKey,Cipher.DECRYPT_MODE,inputStream,outputStream);
    }

    public static void cipherData(String key, int mode, InputStream inputStream, OutputStream outputStream) throws Throwable{
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES");

        if(mode==Cipher.ENCRYPT_MODE){
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream,cipher);
            doWrite(cipherInputStream,outputStream);
        }else if(mode==Cipher.DECRYPT_MODE){
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream,cipher);
            doWrite(inputStream,cipherOutputStream);
        }
    }
    public static void doWrite(InputStream is, OutputStream os) throws IOException{
        byte[] bytes = new byte[64];
        int totalBytes;
        while((totalBytes=is.read(bytes))!=-1){
            os.write(bytes,0,totalBytes);
        }

        os.flush();
        os.close();
        is.close();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            if(actionEvent.getSource()==btnExit){
                int response = JOptionPane.showConfirmDialog(null, "Do you want to exit?",
                        "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response==JOptionPane.YES_OPTION){
                    System.exit(0);}
                else{
                    window.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
            if(actionEvent.getSource()==btnSend){
                new TransferFile();
                window.dispose();
            }
            if(actionEvent.getSource()==btnBrowse1){

                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf document","pdf"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("text document","txt"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf document","docx","xlsx","pptx"));

                fileChooser.setAcceptAllFileFilterUsed(true);
                int result = fileChooser.showOpenDialog(this);
                if(result==JFileChooser.APPROVE_OPTION){
                    selectedFile = fileChooser.getSelectedFile();
                    encrypt_text.setText(selectedFile.getAbsolutePath());
                }
            }
            if(actionEvent.getSource()==btnBrowse2){
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf document","pdf"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("text document","txt"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf document","docx","xlsx","pptx"));

                fileChooser.setAcceptAllFileFilterUsed(true);
                int result = fileChooser.showOpenDialog(this);
                if(result==JFileChooser.APPROVE_OPTION){
                    selectedFile = fileChooser.getSelectedFile();
                    decrypt_text.setText(selectedFile.getAbsolutePath());
                }
            }
            if(actionEvent.getSource()==btnEncrypt){
                encrypt_key=JOptionPane.showInputDialog("Enter 8 digit Key For Encryption");
                //String type
                if(encrypt_key==null) {
                    JOptionPane.showMessageDialog(this,"Enter only 8 Digit key","Error",JOptionPane.ERROR_MESSAGE);
                }
                if(encrypt_key.trim().length()<8) {
                    JOptionPane.showMessageDialog(this,"Enter only 8 Digit key","Error",JOptionPane.ERROR_MESSAGE);
                }
                if(encrypt_key.trim().length()>8)
                    JOptionPane.showMessageDialog(this,"Enter only 8 Digit key","Error",JOptionPane.ERROR_MESSAGE);
                else
                {
                    // encrypt the message

                    FileInputStream fileInputStream = new FileInputStream("C:\\Users\\mcoji\\Documents\\REVISED FINAL ONLINE EXAM TIMETABLE SEM II 2019_2020 FINAL YR STUDENTS.docx");
                    FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\mcoji\\Documents\\REVISED FINAL ONLINE EXAM TIMETABLE SEM II 2019_2020 FINAL YR STUDENTS.docx");
                    encryptData(encrypt_key,fileInputStream,fileOutputStream);
                    JOptionPane.showMessageDialog(this, "encryption completed", "success", JOptionPane.INFORMATION_MESSAGE);

                }
                encrypt_text.setText("");
            }
            if(actionEvent.getSource()==btnDecrypt){
                decrypt_key = JOptionPane.showInputDialog("Enter 8 digit Key For Decryption");
                //String type
                if (decrypt_key.trim().equals(""))
                    JOptionPane.showMessageDialog(this, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    // decrypt the message
                    FileInputStream fileInputStream = new FileInputStream("C:\\Users\\mcoji\\Documents\\REVISED FINAL ONLINE EXAM TIMETABLE SEM II 2019_2020 FINAL YR STUDENTS.docx");
                    FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\mcoji\\Documents\\REVISED FINAL ONLINE EXAM TIMETABLE SEM II 2019_2020 FINAL YR STUDENTS.docx");
                    decryptData(decrypt_key,fileInputStream,fileOutputStream);
                    JOptionPane.showMessageDialog(this, "decryption completed", "success", JOptionPane.INFORMATION_MESSAGE);
                }
                decrypt_text.setText("");
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this,ex,"ERROR",JOptionPane.ERROR_MESSAGE);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
