package com.example.encrypt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class Encrypt {
    public Options createOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false, "show help");
        options.addOption(help);

        Option password = new Option("p", "password", true, "password");
        options.addOption(password);

        Option verify = new Option("v", "verify", true, "verify a hash with password (use with --password.)");
        options.addOption(verify);

        return options;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("bcrypt-cli", createOptions());
    }

    public CommandLine parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();

        return parser.parse(createOptions(), args);
    }

    public void runHash(String plain) {
        String hashed = BCrypt.hashpw(plain, BCrypt.gensalt());
        System.out.println(hashed);
    }

    public void runVerify(String plain, String hashed) {
        if (BCrypt.checkpw(plain, hashed)) {
            System.out.println(plain + " == " + hashed);
        } else {
            System.out.println(plain + " != " + hashed);
        }
    }

    public void run(String[] args) {
        try {
            CommandLine cmd = parse(args);

            if (cmd.hasOption("help")) {
                printHelp();
                System.exit(0);
            }

            if (!cmd.hasOption("password")) {
                throw new Exception("could not find the command line argument `password'. Use --password or -p.");
            }

            if (cmd.hasOption("verify")) {
                runVerify(cmd.getOptionValue("password"), cmd.getOptionValue("verify"));
            } else {
                runHash(cmd.getOptionValue("password"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            printHelp();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Encrypt enc = new Encrypt();

        enc.run(args);
    }
}
