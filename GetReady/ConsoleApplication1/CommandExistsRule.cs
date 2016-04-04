using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    /**
     * Rule to validate that a command has executed previously
     * 
     * If validIfExists is true, returns success if command exists
     * A common case for this is PJs should be removed before all commands
     * 
     * If validIfExists is false, returns success if command doesn't exist
     * A common case for this is the same command should not have executed already
     */
    class CommandExistsRule : Rule
    {
        private Command command;
        private bool validIfExists;
        
        public CommandExistsRule(Command command, bool validIfExists)
        {
            this.command = command;
            this.validIfExists = validIfExists;
        }

        public override bool isValid(List<Command> previousCommands)
        {
            foreach (Command previousCommand in previousCommands)
            {
                if (this.command == previousCommand)
                {
                    return validIfExists;
                }
            }
            return !validIfExists;
        }
    }
}
