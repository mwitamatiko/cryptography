import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
public class TransferFile extends JFrame implements ActionListener {

    private JLabel label = new JLabel("upload file");
    private JTextField jTextField = new JTextField();
    private JButton jbtBrowse = new JButton("load file");
    private JButton jbtSend = new JButton("send");
    private JButton jbtBack = new JButton("back");
    private JPanel panel = new JPanel();
    private JFrame window = new JFrame();

    JFileChooser fileChooser = new JFileChooser();
    File temporaryFilename, outputFilename;
    int countOpenedFile;
    InetAddress ip;
    String address;

    public TransferFile(){
        Font font1 = new Font("quicksand", Font.BOLD, 16);
        window.setDefaultCloseOperation(HIDE_ON_CLOSE);
        window.setResizable(false);
        window.setSize(600,300);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        panel.setLayout(null);
        window.add(panel);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new DesExample();
                window.dispose();
            }
        });

        label.setBounds(10,50,300,30);
        label.setFont(font1);
        panel.add(label);

        jTextField.setBounds(100,50,350,40);
        jTextField.setEditable(false);
        jTextField.setFont(font1);
        panel.add(jTextField);

        jbtBrowse.setBounds(460,50,130,40);
        jbtBrowse.setFont(font1);
        jbtBrowse.addActionListener(this);
        panel.add(jbtBrowse);

        jbtSend.setBounds(200,130,200,30);
        jbtSend.setFont(font1);
        jbtSend.addActionListener(this);
        panel.add(jbtSend);

        jbtBack.setBounds(80,200,400,30);
        jbtBack.setFont(font1);
        jbtBack.addActionListener(this);
        panel.add(jbtBack);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    }
    public void run() {
        try{
            new ReceiveImage();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            if(actionEvent.getSource()==jbtBrowse){
                int r= fileChooser.showOpenDialog(this);
                temporaryFilename = fileChooser.getSelectedFile(); //File type
                if(r==JFileChooser.CANCEL_OPTION)
                    JOptionPane.showMessageDialog(this,"FileNotSelected","Error",JOptionPane.ERROR_MESSAGE);
                else{
                    String name= temporaryFilename.getName();
                    if((!name.endsWith(".pdf")) && (!name.endsWith(".doc")) && (!name.endsWith(".docx")) && (!name.endsWith(".txt")))
                        JOptionPane.showMessageDialog(this,"Select Only file e.g .pdf,.docx,.txt,.doc","Error",JOptionPane.ERROR_MESSAGE);
                    else
                    {
                        countOpenedFile =1;
                        outputFilename = temporaryFilename;
                        jTextField.setText(name);
                        jTextField.setText(temporaryFilename.getPath());
                        jTextField.setFont(new Font("quicksand",Font.PLAIN,15));
                    }
                }
            }
            if(actionEvent.getSource()==jbtSend){
                address=JOptionPane.showInputDialog("please enter an ip address of the receiver");
                ip =InetAddress.getByName(address);
                Socket socket=new Socket(ip,8000);
                DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                FileInputStream fileInputStream=new FileInputStream(outputFilename);
                while(true) {
                    int i=fileInputStream.read();
                    if(i==-1) break;
                    dataOutputStream.write(i);
                }
                fileInputStream.close();
                dataOutputStream.close();
                jTextField.setText("");
            }
            if(actionEvent.getSource()==jbtBack){
                new DesExample();
                window.dispose();
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this,ex,"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

}
