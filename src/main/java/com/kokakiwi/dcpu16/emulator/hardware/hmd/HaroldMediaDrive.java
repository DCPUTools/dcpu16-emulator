package com.kokakiwi.dcpu16.emulator.hardware.hmd;

import de.codesourcery.jasm16.Address;
import de.codesourcery.jasm16.Register;
import de.codesourcery.jasm16.emulator.IEmulator;
import de.codesourcery.jasm16.emulator.devices.DeviceDescriptor;
import de.codesourcery.jasm16.emulator.devices.IDevice;

public class HaroldMediaDrive implements IDevice
{
    public final static DeviceDescriptor DESCRIPTOR             = new DeviceDescriptor(
                                                                        "Harold Media Drive",
                                                                        "The HMD2043 is the latest effort on the part of Harold Innovation Technologies",
                                                                        0x74fa4cae,
                                                                        0x07c2,
                                                                        0x21544948);
    
    public final static int              NON_BLOCKING           = 1;
    public final static int              MEDIA_STATUS_INTERRUPT = 2;
    
    /**
     * The operation either completed or (for non-blocking operations) begun
     * successfully.
     */
    public final static int              ERROR_NONE             = 0x0000;
    /**
     * Operation requires media to be present. In long operations, this can
     * occur if the media is ejected during the operation.
     */
    public final static int              ERROR_NO_MEDIA         = 0x0001;
    /**
     * Attempted to read or write to an invalid sector number.
     */
    public final static int              ERROR_INVALID_SECTOR   = 0x0002;
    /**
     * Attempted to perform a non-blocking operation whilst a conflicting
     * operation was already in progress: the most recent operation has been
     * aborted.
     */
    public final static int              ERROR_PENDING          = 0x0003;
    
    private HaroldMediaUnit              media                  = null;
    
    private int                          flag                   = 0;
    private int                          interruptMessage       = 0xffff;
    
    public int handleInterrupt(IEmulator emulator)
    {
        int a = emulator.getCPU().getRegisterValue(Register.A);
        int b = emulator.getCPU().getRegisterValue(Register.B);
        int c = emulator.getCPU().getRegisterValue(Register.C);
        int x = emulator.getCPU().getRegisterValue(Register.X);
        
        switch (a & 0xffff)
        {
            case 0x0000:
                b = (media == null) ? 0 : 1;
                a = ERROR_NONE;
                break;
            
            case 0x0001:
                if (media != null)
                {
                    a = ERROR_NONE;
                    b = media.getWordsPerSector();
                    c = media.getSectors();
                    x = media.isWriteLocked() ? 1 : 0;
                }
                else
                {
                    a = ERROR_NO_MEDIA;
                }
                break;
            
            case 0x0002:
                a = ERROR_NONE;
                b = flag;
                break;
            
            case 0x0003:
                a = ERROR_NONE;
                flag = b;
                break;
            
            case 0x0004:
                a = ERROR_PENDING;
                break;
            
            case 0x0005:
                a = ERROR_NONE;
                interruptMessage = b;
                break;
            
            case 0x0010:
                if (media != null)
                {
                    a = media.read(emulator.getMemory(),
                            Address.wordAddress(x), b, c);
                }
                else
                {
                    a = ERROR_NO_MEDIA;
                }
                break;
            
            case 0x0011:
                if (media != null)
                {
                    a = media.write(emulator.getMemory(),
                            Address.wordAddress(x), b, c);
                }
                else
                {
                    a = ERROR_NO_MEDIA;
                }
                break;
            
            case 0xffff:
                if (media != null)
                {
                    a = ERROR_NONE;
                    b = media.getQuality();
                }
                else
                {
                    a = ERROR_NO_MEDIA;
                }
                break;
        }
        
        emulator.getCPU().setRegisterValue(Register.A, a);
        emulator.getCPU().setRegisterValue(Register.B, b);
        emulator.getCPU().setRegisterValue(Register.C, c);
        emulator.getCPU().setRegisterValue(Register.X, x);
        
        return 1;
    }
    
    public void afterAddDevice(IEmulator emulator)
    {
        
    }
    
    public void reset()
    {
        media = new HaroldMediaUnit(false);
    }
    
    public void beforeRemoveDevice(IEmulator emulator)
    {
        
    }
    
    public DeviceDescriptor getDeviceDescriptor()
    {
        return DESCRIPTOR;
    }
    
}
