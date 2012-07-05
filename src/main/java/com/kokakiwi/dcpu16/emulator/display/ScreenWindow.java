package com.kokakiwi.dcpu16.emulator.display;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.kokakiwi.dcpu16.emulator.DCPUEmulator;

import de.codesourcery.jasm16.emulator.devices.impl.DefaultScreen;

public class ScreenWindow extends JFrame
{
    private static final long  serialVersionUID = -7526373821197121620L;
    
    public final static int    WIDTH            = (DefaultScreen.STANDARD_SCREEN_COLUMNS
                                                        * DefaultScreen.GLYPH_WIDTH + DefaultScreen.BORDER_WIDTH) * 4;
    public final static int    HEIGHT           = (DefaultScreen.STANDARD_SCREEN_ROWS
                                                        * DefaultScreen.GLYPH_HEIGHT + DefaultScreen.BORDER_HEIGHT) * 4;
    
    private final ScreenPanel  panel;
    
    public ScreenWindow(final DCPUEmulator emulator)
    {
        super("Screen");
        panel = new ScreenPanel(emulator);
        
        add(panel);
        
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(new Dimension(WIDTH, HEIGHT));
        
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e)
            {
                emulator.stop();
            }
        });
    }
}
