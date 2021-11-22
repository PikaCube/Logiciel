package fr.pikacube.logiciel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Fenetre extends JFrame implements ActionListener {

    static JComboBox<String> utilisateurs;
    JButton supprimer;
    JButton admin;
    JButton ajouter;
    static SQL sql;
    Logs messages;

    public Fenetre (){
        setSize(200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        sql = new SQL();
        messages = new Logs();

        utilisateurs = new JComboBox<>();
        utilisateurs.setBounds(40,10,100,50);
        updateList();

        supprimer = new JButton("Supprimer");
        supprimer.setBounds(40,70,100,15);
        supprimer.setContentAreaFilled(false);
        supprimer.setForeground(Color.RED);
        supprimer.addActionListener(this);
        supprimer.setVisible(true);

        ajouter = new JButton("Ajouter");
        ajouter.setBounds(40,90,100,15);
        ajouter.setContentAreaFilled(false);
        ajouter.addActionListener(this);
        ajouter.setForeground(Color.decode("#00750a"));
        ajouter.setVisible(true);

        admin = new JButton("Grade");
        admin.setBounds(40,110,100,15);
        admin.setContentAreaFilled(false);
        admin.addActionListener(this);
        admin.setForeground(Color.decode("#1702fa"));
        admin.setVisible(true);

        addToContentPanel(utilisateurs);
        addToContentPanel(supprimer);
        addToContentPanel(ajouter);
        addToContentPanel(admin);

        setVisible(true);
    }

    private void addToContentPanel(Component component){
        getContentPane().add(component);
    }

    static void updateList()
    {
        if(utilisateurs != null)
        {
            if(utilisateurs.getItemCount() > 0)
            {
                utilisateurs.removeAllItems();
            }

            if(sql.getUsers() != null && !sql.getUsers().isEmpty())
            {
                sql.getUsers().forEach((users) -> {
                    utilisateurs.addItem(users);
                });
            }else{ utilisateurs.addItem("BDD Vide !"); }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
                if(e.getSource() == ajouter) //Permet d'ajouter un utilisateur à la BDD :
                {
                    String user = JOptionPane.showInputDialog("Quel pseudo ajouter ?");
                    String password = JOptionPane.showInputDialog("Quel sera sont mot de passe ?");
                    if(user != null && password != null)
                    {
                        if(sql.selectQuery("SELECT pseudo FROM utilisateurs WHERE pseudo ='"+user+"'") == null)
                        {
                            sql.addUser(user, password);
                            messages.successMessage("Utilisateur créer !");
                        }
                        else
                        {
                            messages.errorMessage("Cet utilisateur existe déjà !");
                        }
                    }
                }

                if(e.getSource() == supprimer) //permet de supprimer un utilisateur de la BDD :
                {
                    String pseudo =utilisateurs.getSelectedItem().toString();
                    if(sql.selectQuery("SELECT pseudo FROM utilisateurs WHERE pseudo ='"+pseudo+"'") == null)
                    {
                        messages.errorMessage("Cet pseudo n'existe pas !");
                    }
                    else
                    {
                        sql.deleteUser(pseudo);
                        messages.successMessage(pseudo +" a été supprimé !");
                    }
                }

                if(e.getSource() == admin) //permet de définir un utilisateur en tant qu'admin :
                {
                    String pseudo = utilisateurs.getSelectedItem().toString();
                    if(sql.getUsers().contains(pseudo))
                    {
                        Object[] grades = {"Administrateur", "Utilisateur"};
                        String s = (String)JOptionPane.showInputDialog(
                                this,
                                "Quel grade attribuer à "+pseudo+" ?",
                                "Choisir un Grade",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                grades,
                                "Utilisateur");

                        if ((s != null) && (s.length() > 0)) {
                            sql.modifyStatus(pseudo, s);
                            messages.successMessage(pseudo + " est maintenant : "+s);
                            return;
                        }
                        else
                        {
                            messages.errorMessage("Vous devez choisir un grade !");
                        }
                    }
                    else{
                        messages.errorMessage("Veuillez d'abord créer cet utilisateur.");
                        updateList();
                    }
                }
    }
}
