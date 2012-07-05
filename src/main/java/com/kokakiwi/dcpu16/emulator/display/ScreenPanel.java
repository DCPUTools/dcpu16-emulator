package com.kokakiwi.dcpu16.emulator.display;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.kokakiwi.dcpu16.emulator.DCPUEmulator;

import de.codesourcery.jasm16.emulator.devices.impl.DefaultKeyboard;
import de.codesourcery.jasm16.emulator.devices.impl.DefaultScreen;

public class ScreenPanel extends JPanel
{
    private static final long   serialVersionUID = -968001393256223775L;
    
    private final DefaultScreen screen;
    
    public ScreenPanel(DCPUEmulator emulator)
    {
        super();
        this.screen = emulator.getHardware().getDevice("screen");
        
        DefaultKeyboard keyboard = emulator.getHardware().getDevice("keyboard");
        
        keyboard.setInputComponent(this);
        screen.attach(this);
        
        setDoubleBuffered(true);
        setFocusable(true);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        
        BufferedImage image = screen.getScreenImage();
        
        if (image != null)
        {
            final Image scaled = image.getScaledInstance(getWidth(),
                    getHeight(), Image.SCALE_FAST);
            g.drawImage(scaled, 0, 0, null);
        }
    }
}
