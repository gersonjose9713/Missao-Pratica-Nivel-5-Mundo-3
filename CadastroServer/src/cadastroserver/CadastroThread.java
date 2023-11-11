/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import java.io.*;
import java.net.Socket;
import controller.UsuariosJpaController;
import controller.ProdutosJpaController;

/**
 *
 * @author Kypz
 */

public class CadastroThread extends Thread {
    private final ProdutosJpaController ctrl;
    private final UsuariosJpaController ctrlUsu;
    private final Socket s1;

    public CadastroThread(ProdutosJpaController ctrl, UsuariosJpaController ctrlUsu, Socket socket) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s1.getInputStream());

            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            
            if (isValidUser(login, senha)) {
                out.writeObject("Login efetuado com sucesso");               
            } else {
                try (s1) {
                    out.writeObject("Credenciais inválidas. Tente novamente");
                }
            }
            String command;
            while ((command = (String) in.readObject()) != null) {
                    if (command.equals("L")) {
                        out.writeObject(ctrl.getProdutos());
                    }
                }
                           
        } catch (IOException | ClassNotFoundException e) {
        }
                
    }

    private boolean isValidUser(String login, String senha) {
        return ctrlUsu.findUsuario(login, senha) != null;
    }
}
