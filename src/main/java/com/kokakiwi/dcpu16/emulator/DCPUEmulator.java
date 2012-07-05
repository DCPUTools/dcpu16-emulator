package com.kokakiwi.dcpu16.emulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.kokakiwi.dcpu16.emulator.display.ScreenWindow;
import com.kokakiwi.dcpu16.emulator.hardware.Hardware;
import com.kokakiwi.dcpu16.emulator.hardware.hmd.HaroldMediaDrive;

import de.codesourcery.jasm16.Address;
import de.codesourcery.jasm16.disassembler.DisassembledLine;
import de.codesourcery.jasm16.disassembler.Disassembler;
import de.codesourcery.jasm16.emulator.Emulator;
import de.codesourcery.jasm16.emulator.IEmulator.EmulationSpeed;

public class DCPUEmulator
{
    @Option(name = "-f", aliases = { "--file" }, required = true)
    private File               file;
    
    @Option(name = "-s", aliases = { "--speed" })
    private EmulationSpeed     speed       = EmulationSpeed.REAL_SPEED;
    
    @Option(name = "-d", aliases = { "--disassemble" })
    private boolean            disassemble = false;
    
    private final Emulator     emulator;
    private final Hardware     hardware;
    
    private final ScreenWindow window;
    
    public DCPUEmulator()
    {
        emulator = new Emulator();
        hardware = new Hardware(emulator);
        
        hardware.loadDefaults();
        
        hardware.registerDevice("hmd", new HaroldMediaDrive());
        
        window = new ScreenWindow(this);
    }
    
    public File getFile()
    {
        return file;
    }
    
    public EmulationSpeed getSpeed()
    {
        return speed;
    }
    
    public boolean isDisassemble()
    {
        return disassemble;
    }
    
    public Emulator getEmulator()
    {
        return emulator;
    }
    
    public ScreenWindow getWindow()
    {
        return window;
    }
    
    public Hardware getHardware()
    {
        return hardware;
    }
    
    public void start()
    {
        emulator.reset(false);
        
        // Set speed
        emulator.setEmulationSpeed(speed);
        
        // Load file
        try
        {
            byte[] data = IOUtils.toByteArray(new FileInputStream(file));
            emulator.loadMemory(Address.ZERO, data);
            
            if (disassemble)
            {
                Disassembler disassembler = new Disassembler();
                List<DisassembledLine> lines = disassembler.disassemble(data,
                        false);
                
                StringBuilder sb = new StringBuilder();
                for (DisassembledLine line : lines)
                {
                    sb.append(line.getAddress().toString());
                    sb.append(" : ");
                    sb.append(line.getContents());
                    sb.append('\n');
                }
                FileOutputStream out = new FileOutputStream(new File(
                        "disassembled.txt"));
                IOUtils.write(sb.toString(), out);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        window.setVisible(true);
        emulator.start();
    }
    
    public void stop()
    {
        window.setVisible(false);
        emulator.stop();
        
        System.exit(0);
    }
    
    public static void main(String[] args)
    {
        DCPUEmulator emulator = new DCPUEmulator();
        CmdLineParser parser = new CmdLineParser(emulator);
        try
        {
            parser.parseArgument(args);
            emulator.start();
        }
        catch (CmdLineException e)
        {
            e.printStackTrace();
            parser.printUsage(System.out);
        }
    }
}
