package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

public class RedesController {
    private Process initProcess(String process) {
        try {
            return Runtime.getRuntime().exec(process);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private String os() {
        return System.getProperty("os.name");
    }

    public void ip() {
        String command = "";
        if(os().contains("Windows"))
            command = "ipconfig";
        if(os().contains("Linux"))
            command = "ifconfig";
        
        Process process = initProcess(command);

        InputStreamReader stream = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(stream);

        // nome do adaptador
        String adaptador = "";
        // buffer para acumular as informações para impressão futura
        String info_accumulator = "";
        try {
            String line = buffer.readLine();
            while(line != null) {
                if(line.contains("Adaptador"))
                    adaptador = line;
                if(line.contains("IPv4") || line.contains("inet"))
                    info_accumulator += adaptador + "\n" + line + "\n";
                line = buffer.readLine();
            }
            JOptionPane.showMessageDialog(null, info_accumulator);
        }catch(IOException e) {
            System.err.println("Chamada inválida");
        }
    }

    public void ping() {
        String command = "";
        if(os().contains("Windows"))
            command = "ping -4 -n 10 www.google.com.br";
        if(os().contains("Linux"))
            command = "ping -4 -c 10 www.google.com.br";
        
        Process process = initProcess(command);

        InputStreamReader stream = new InputStreamReader(process.getInputStream());
        BufferedReader buffer = new BufferedReader(stream);

        try {
            String line = buffer.readLine();
            
            while(line != null) {
                    
                System.out.println(line);
                // Pattern considera apenas Windows no idioma pt_BR
                if(line.contains("dia")) {
                    String []lineVector = line.split("ms");
                    int size = lineVector.length;
                    String media = lineVector[size-1].replaceAll("\\D+", "");
                    JOptionPane.showMessageDialog(null, "Média: " + media + "ms");
                }
                // Pattern considera apenas Linux no idioma US_en.
                if(line.contains("rtt")) {
                	String []lineVector = line.split(" ");
                	int size = lineVector.length;
                	String media = lineVector[size-2].split("/")[1];
                	JOptionPane.showMessageDialog(null, "\nMédia: " + media + "ms");
                }
                line = buffer.readLine();
            }

        }catch(IOException e) {
            System.err.println("Chamada inválida");
        }
    }
}
