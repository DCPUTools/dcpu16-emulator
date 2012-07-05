package com.kokakiwi.dcpu16.emulator.hardware.hmd;

import java.nio.IntBuffer;

import de.codesourcery.jasm16.Address;
import de.codesourcery.jasm16.Size;
import de.codesourcery.jasm16.emulator.memory.IMemory;

public class HaroldMediaUnit
{
    public final static int DEFAULT_WORDS_PER_SECTOR = 512;
    public final static int DEFAULT_SECTORS          = 1440 * 2;
    
    public final static int QUALITY_AUTHENTIC        = 0x7fff;
    public final static int QUALITY_OTHER            = 0xffff;
    
    public final static int DEFAULT_QUALITY          = QUALITY_AUTHENTIC;
    
    private final int       wordsPerSector;
    private final int       sectors;
    private final int       quality;
    private boolean         writeLocked;
    
    private int[]           data;
    
    public HaroldMediaUnit(boolean writeLocked)
    {
        this(DEFAULT_WORDS_PER_SECTOR, DEFAULT_SECTORS, DEFAULT_QUALITY,
                writeLocked);
    }
    
    public HaroldMediaUnit(int wordsPerSector, int sectors, int quality,
            boolean writeLocked)
    {
        this.wordsPerSector = wordsPerSector;
        this.sectors = sectors;
        this.quality = quality;
        this.writeLocked = writeLocked;
        
        data = new int[sectors * wordsPerSector];
    }
    
    public int getWordsPerSector()
    {
        return wordsPerSector;
    }
    
    public int getSectors()
    {
        return sectors;
    }
    
    public int getQuality()
    {
        return quality;
    }
    
    public boolean isWriteLocked()
    {
        return writeLocked;
    }
    
    public void setWriteLocked(boolean writeLocked)
    {
        this.writeLocked = writeLocked;
    }
    
    public int read(IMemory memory, Address offset, int sector, int size)
    {
        int code = HaroldMediaDrive.ERROR_NONE;
        int length = size * wordsPerSector;
        
        IntBuffer buffer = read(sector, size);
        for (int i = 0; i < length; i++)
        {
            memory.write(offset.plus(Size.words(i), true), buffer.get(i));
        }
        
        return code;
    }
    
    public IntBuffer read(int sector, int size)
    {
        int offset = sector * wordsPerSector;
        int length = size * wordsPerSector;
        
        IntBuffer buffer = IntBuffer.wrap(data, offset, length);
        
        return buffer;
    }
    
    public int write(IMemory memory, Address offset, int sector, int size)
    {
        int code = HaroldMediaDrive.ERROR_NONE;
        int off = sector * wordsPerSector;
        int length = size * wordsPerSector;
        
        if (off < data.length)
        {
            for (int i = 0; i < length; i++)
            {
                data[off + i] = memory.read(offset.plus(Size.words(i), true));
            }
        }
        else
        {
            code = HaroldMediaDrive.ERROR_INVALID_SECTOR;
        }
        
        return code;
    }
}
