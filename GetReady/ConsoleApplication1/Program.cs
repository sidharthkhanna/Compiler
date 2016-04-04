using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GetReady
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 1)
            {
                throw new ArgumentOutOfRangeException("args", "args length should be > 0");
            }

            // Find temerature type by parsing the first command line argument
            string argTemperatureTypeString = args[0];
            bool temperatureTypeFound = false;
            TemperatureType currentTemperatureType = TemperatureType.HOT;
            foreach (TemperatureType temperatureType in Enum.GetValues(typeof(TemperatureType)))
            {
                if (argTemperatureTypeString == temperatureType.ToString())
                {
                    currentTemperatureType = temperatureType;
                    temperatureTypeFound = true;
                    break;
                }
            }

            if (temperatureTypeFound == false)
            {
                throw new ArgumentException("Invalid temperature type", argTemperatureTypeString);
            }

            RuleValidator ruleValidator = new RuleValidator(currentTemperatureType);
            ResponseGenerator responseGenerator = new ResponseGenerator();

            // For each command, execute rule and write response to command line
            for (int i = 1; i < args.Length; i++)
            {
                string arg = args[i];
                // Remove comma and get the command value
                string commandValue = arg.Split(',')[0];
                Command command = (Command)Enum.Parse(typeof(Command), commandValue);
                bool isCommandValid = ruleValidator.validateCommand(command);
                if (isCommandValid)
                {
                    Console.Write(responseGenerator.getResponse(currentTemperatureType, command));
                    if (i != args.Length - 1)
                    {
                        Console.Write(", ");
                    }
                }
                else
                {
                    Console.Write("fail");
                    break;
                }
            }
            Console.ReadLine();
        }
    }
}
