using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    /**
     * Class used to generate the response string based on the command and temerature type
     */
    class ResponseGenerator
    {
        private class CommandAndTemperatureType
        {
            Command command;
            TemperatureType temperatureType;

            public CommandAndTemperatureType(Command command, TemperatureType temperatureType)
            {
                this.command = command;
                this.temperatureType = temperatureType;
            }

            public override string ToString()
            {
                return this.command.ToString() + this.temperatureType.ToString();
            }
        }

        private Dictionary<string, string> commandToResponseMap =
            new Dictionary<string, string>();

        public ResponseGenerator()
        {
            generateResponseRules();
        }

        public string getResponse(TemperatureType temperatureType, Command command)
        {
            string response = null;
            bool responseFound = commandToResponseMap.TryGetValue(
                new CommandAndTemperatureType(command, temperatureType).ToString(), out response);
            if (responseFound)
            {
                return response;
            }
            return null;
        }

        private void generateResponseRules()
        {
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.FootWear, TemperatureType.HOT).ToString(), "sandals");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.FootWear, TemperatureType.COLD).ToString(), "boots");
            
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.HeadWear, TemperatureType.HOT).ToString(), "sun visor");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.HeadWear, TemperatureType.COLD).ToString(), "hat");
            
            // Don't add a response for socks and hot temperature type
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Socks, TemperatureType.COLD).ToString(), "socks");
            
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Shirt, TemperatureType.HOT).ToString(), "t-shirt");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Shirt, TemperatureType.COLD).ToString(), "shirt");

            // Don't add a response for jacket and hot temperature type
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Jacket, TemperatureType.COLD).ToString(), "jacket");

            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Pants, TemperatureType.HOT).ToString(), "shorts");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Pants, TemperatureType.COLD).ToString(), "pants");

            commandToResponseMap.Add(new CommandAndTemperatureType(Command.LeaveHouse, TemperatureType.HOT).ToString(), "leaving house");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.LeaveHouse, TemperatureType.COLD).ToString(), "leaving house");
            
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Pajamas, TemperatureType.HOT).ToString(), "Removing PJs");
            commandToResponseMap.Add(new CommandAndTemperatureType(Command.Pajamas, TemperatureType.COLD).ToString(), "Removing PJs");
        }
    }
}
