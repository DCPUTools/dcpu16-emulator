package com.kokakiwi.dcpu16.emulator.args;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import de.codesourcery.jasm16.emulator.IEmulator.EmulationSpeed;

public class EmulationSpeedOptionHandler extends OptionHandler<EmulationSpeed>
{
    
    protected EmulationSpeedOptionHandler(CmdLineParser parser,
            OptionDef option, Setter<? super EmulationSpeed> setter)
    {
        super(parser, option, setter);
    }
    
    @Override
    public int parseArguments(Parameters params) throws CmdLineException
    {
        String value = params.getParameter(0);
        EmulationSpeed speed = EmulationSpeed.REAL_SPEED;
        if (value.equalsIgnoreCase("max"))
        {
            speed = EmulationSpeed.MAX_SPEED;
        }
        
        setter.addValue(speed);
        
        return 1;
    }
    
    @Override
    public String getDefaultMetaVariable()
    {
        return "EMUSPEED";
    }
    
}
