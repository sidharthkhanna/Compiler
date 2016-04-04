using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    /**
     * Validates that a command should not exist in input
     * 
     */
    class InvalidCommandRule : Rule
    {
        public override bool isValid(List<Command> previousCommands)
        {
            // If this rule exists, it means the command itself is not valid
            // Just return false
            return false;
        }
    }
}
