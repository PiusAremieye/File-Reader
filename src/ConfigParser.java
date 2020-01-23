import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

class ConfigParser{
	private String nameOfFile;
	private HashMap<String,String> inputFile;

	public ConfigParser(String nameOfFile) {
		this.nameOfFile = nameOfFile;
//		this.nameOfFile = "./src/" + nameOfFile;
		this.inputFile  = new HashMap<>();

		Path path = Paths.get(this.nameOfFile);
		try {
			String[] values;
			boolean stateOfLine = true;
			int count = 0;

			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String line : lines) {
				System.out.println(line);
				line.replace("\n", "");
				if (!line.isEmpty()) {
					if (line.equals("[application]")) {
						stateOfLine = false;
					}
					if (stateOfLine) {
						values = line.split("=");
						String key = values[0];
						String value = values[1];
						this.inputFile.put(key, value);
					}
					if (!stateOfLine && count < 3 && !line.equals("[application]")) {
						values = line.split("=");
						String key = "application." + values[0];
						String value = values[1];
						this.inputFile.put(key, value);
						count++;
					}
					if (count == 3) {
						stateOfLine = true;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}

	public void getContext(String keys) {
		System.out.println(inputFile.get(keys.toLowerCase()));
	}

	public void getNameOfFile() {
		System.out.println(nameOfFile);
	}

	public void getInputFile() {
		System.out.println(inputFile);
	}

	public static void main(String[] args) {
		if (args.length == 0){
			ConfigParser configParser = new ConfigParser("config.txt");
			System.out.println("======================================");
			configParser.getContext("application.context-url");
		}
		else if (args[0].equalsIgnoreCase("dev")) {
			ConfigParser configParser = new ConfigParser("config.txt.dev");
			System.out.println("======================================");
			configParser.getContext("name");
			configParser.getInputFile();
		}else if (args[0].equalsIgnoreCase("staging")) {
			ConfigParser configParser = new ConfigParser("config.txt.staging");
			System.out.println("======================================");
			configParser.getContext("name");
		}else{
			System.out.println("Invalid file");
		}
//		ConfigParser configParser = new ConfigParser("config.txt");

	}
}
