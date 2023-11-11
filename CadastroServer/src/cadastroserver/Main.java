/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import controller.ProdutosJpaController;
import controller.UsuariosJpaController;

/**
 *
 * @author Kypz
 */
public class Main {
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        
        ProdutosJpaController ctrlProduto = new ProdutosJpaController(emf);
        UsuariosJpaController ctrlUsuario = new UsuariosJpaController(emf);
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4321);
            System.out.println("Aguardando conexoes");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
               
                Socket socket = serverSocket.accept();
             
                CadastroThread cadastroThread = new CadastroThread(ctrlProduto, ctrlUsuario, socket);
                cadastroThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
