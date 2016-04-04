using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    /**
     * Class used to validate the rules for a command
     */
    class RuleValidator
    {
        private TemperatureType temperatureType;
        private Dictionary<Command, List<Rule>> commandToRules = new Dictionary<Command, List<Rule>>();
        private List<Command> previousCommands = new List<Command>();

        public RuleValidator(TemperatureType temperatureType)
        {
            this.temperatureType = temperatureType;
            createRules();
        }

        /**
         * Gets all the rules for a command and validates them
         * Some rules need the list of previous commands executed for eg
         * to check a command hasn't already been executed
         * This function also adds the command to the list of previous commands
         * to be used for future commands
         */
        public bool validateCommand(Command command)
        {
            List<Rule> rules;
            commandToRules.TryGetValue(command, out rules);
            Boolean isValid = true;
            foreach (Rule rule in rules)
            {
                if (!rule.isValid(previousCommands))
                {
                    isValid = false;
                }
            }
            previousCommands.Add(command);
            return isValid;
        }

        /**
         * Create all the rules as mentioned in the problem
         */
        private void createRules()
        {
            CommandExistsRule pajamasRemovedRule = new CommandExistsRule(Command.Pajamas, true);
            foreach (Command command in Enum.GetValues(typeof(Command)))
            {
                // Add rule for pajamas removed before all commands
                if (command != Command.Pajamas)
                {
                    addRule(command, pajamasRemovedRule);
                }

                // Add rule for same command not executed already
                addRule(command, new CommandExistsRule(command, false));
                
                // Add rule that all commands should be executed before leaving house
                if (command != Command.LeaveHouse && command != Command.Pajamas)
                {
                    if ((command == Command.Socks || command == Command.Jacket)
                        && temperatureType == TemperatureType.HOT)
                    {
                        // If temperature type is hot, don't add rule for socks and jacket
                        continue;
                    }
                    addRule(Command.LeaveHouse, new CommandExistsRule(command, true));
                }
            }

            // Add rule for not wearing socks, jacket in Hot temperature
            if (temperatureType == TemperatureType.HOT)
            {
                InvalidCommandRule invalidCommandRule = new InvalidCommandRule();
                addRule(Command.Socks, invalidCommandRule);
                addRule(Command.Jacket, invalidCommandRule);
            }

            // Add rule for wearing socks before footwear if teperature type is not hot
            // because socks are not worn in hot weather
            if (temperatureType != TemperatureType.HOT)
            {
                addRule(Command.FootWear, new CommandExistsRule(Command.Socks, true));
            }

            // Add rule for wearing pants before footwear
            addRule(Command.FootWear, new CommandExistsRule(Command.Pants, true));
            
            // Add rule for wearing shirt before headwear and jacket
            CommandExistsRule shirtWornRule = new CommandExistsRule(Command.Shirt, true);
            addRule(Command.HeadWear, shirtWornRule);
            // Add rule for jacket if weather is not hot
            if (temperatureType != TemperatureType.HOT)
            {
                addRule(Command.Jacket, shirtWornRule);
            }
        }

        private void addRule(Command command, Rule rule)
        {
            List<Rule> existingRules;
            commandToRules.TryGetValue(command, out existingRules);
            if (existingRules == null)
            {
                existingRules = new List<Rule>();
            }
            if (existingRules.Contains(rule))
            {
                throw new InvalidOperationException("Cannot add the same rule twice. This is caused due to a code bug");
            }
            existingRules.Add(rule);

            commandToRules[command] = existingRules;
        }
    }
}
