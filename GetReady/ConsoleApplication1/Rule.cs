using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    /**
     * Abstract class for rules
     * Derived classes must implement validation
     */
    abstract class Rule
    {
        public abstract bool isValid(List<Command> previousCommands);
    }
}
