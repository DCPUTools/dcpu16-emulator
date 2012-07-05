package com.kokakiwi.dcpu16.emulator.hardware;

import java.util.Map;

import com.google.common.collect.Maps;

import de.codesourcery.jasm16.emulator.Emulator;
import de.codesourcery.jasm16.emulator.devices.IDevice;
import de.codesourcery.jasm16.emulator.devices.impl.*;

public class Hardware
{
    private final Emulator             emulator;
    private final Map<String, IDevice> devices = Maps.newLinkedHashMap();
    
    public Hardware(Emulator emulator)
    {
        this.emulator = emulator;
    }
    
    public void loadDefaults()
    {
        registerDevice("clock", new DefaultClock());
        registerDevice("keyboard", new DefaultKeyboard(false));
        registerDevice("screen", new DefaultScreen(true, false));
    }
    
    public void registerDevice(String id, IDevice device)
    {
        devices.put(id, device);
        
        emulator.addDevice(device);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IDevice> T getDevice(String id)
    {
        return (T) devices.get(id);
    }
}
