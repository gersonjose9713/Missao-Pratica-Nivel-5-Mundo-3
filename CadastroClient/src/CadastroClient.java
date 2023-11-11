

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;
import model.Produtos;

/**
 *
 * @author Kypz
 */
public class CadastroClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("------------------------------");
            System.out.print("Login: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();
            System.out.print("Enviar comando: ");
            String mensagem = reader.readLine();
            out.writeObject(login);
            out.writeObject(senha);
            out.writeObject(mensagem);

            System.out.println(in.readObject());
            Object objetoRecebido = in.readObject();

            if (true) {
                Vector<Produtos> produtoslist = (Vector<Produtos>) objetoRecebido;
                for (Produtos produto : produtoslist) {
                    System.out.println(produto.getNome());
                }
            }

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
