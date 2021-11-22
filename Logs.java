package fr.pikacube.logiciel;

import javax.swing.*;
import java.awt.*;

public class Logs {

    public JFrame frame;

    public Logs()
    {
        frame = new JFrame();
    }

    public void errorMessage(String message)
    {
        setColor(Color.RED);
        JOptionPane.showMessageDialog(null,message,"Erreur",
                JOptionPane.ERROR_MESSAGE);
    }

    public void infoMessage(String message)
    {
        setColor(Color.BLUE);
        JOptionPane.showMessageDialog(null,message,"Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void successMessage(String message)
    {
        setColor(Color.getColor("#00750a"));
        JOptionPane.showMessageDialog(null,message,"Succès",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void setColor(Color couleur)
    {
        UIManager um = new UIManager();
        um.put("OptionPane.messageForeground", couleur);
    }
}
