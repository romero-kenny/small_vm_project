//Project made by: Kenneth Romero - Feb 2023
//For: CSCI 4200 A1 - Dr. Salimi
//SmallVM Project
//read file line by line and do something based on line.
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.io.File;

public class smallVMProject {
    //creating variables used through program
    static String name_of_student = "Kenneth Romero, CSCI 4200 A1, Spring 2023";
    static Scanner user_pending_input = new Scanner(System.in);
    static Scanner user_file;
    static String file_path = null;
    static final int MAX_MEM_SIZE = 500;
    static String[] allocated_memory = new String[MAX_MEM_SIZE];
    static HashMap<String, Integer> variable_values = new HashMap<String, Integer>();

    //used to clear memory before each use, just incase there's junk data
    static void clear_memory() {
        for (int i = 0; i < allocated_memory.length; i++) {
            allocated_memory[i] = null;
        }
    }

    static void keyword_handler(List<String> tokenized_commands) {
        String keyword = tokenized_commands.get(0);

        switch (keyword) {
            case "ADD", "SUB", "MUL", "DIV":
                math_logic(tokenized_commands);
                break;

            case "IN", "HALT", "STO", "OUT":
                other_commands_logic(tokenized_commands);
                break;

            default:
                throw new IllegalArgumentException("Invalid keyword: " + keyword);
        }

    }

    //gets variable value from hashmap
    static Integer get_variable_value(String var_name) {
        if (variable_values.containsKey(var_name)) {
            return variable_values.get(var_name);
        } else {
            return 0;
        }
    }

    static void other_commands_logic(List<String> tokenized_commands) {
        String keyword = tokenized_commands.get(0);

        if (keyword.equals("HALT")) {
            System.exit(0);
        } 

        String dest_var = tokenized_commands.get(1);
        switch (keyword) {
            case "IN":
                String temp_input = user_pending_input.nextLine();
                variable_allocator(dest_var, Integer.parseInt(temp_input));
                break;

            case "OUT":
                if (is_in_mem(dest_var)) {
                    System.out.println(get_variable_value(dest_var));
                } else {
                    tokenized_commands.subList(1, tokenized_commands.size())
                            .stream()
                            .map(word -> word + " ")
                            .forEach(System.out::print);
                    System.out.println();
                }
                break;

            case "STO":
                //I'm pretty stupid huh lol
                if (is_in_mem(dest_var)) {
                    variable_values.put(dest_var, get_variable_value(dest_var));
                } else {
                    variable_values.put(dest_var, Integer.parseInt(tokenized_commands.get(2)));
                }
                break;
        }
    }

    static Boolean is_in_mem(String var_name) {
        return variable_values.containsKey(var_name);
    }

    static void math_logic(List<String> tokenized_commands) {
        String keyword = tokenized_commands.get(0);
        String destination = tokenized_commands.get(1);
        Optional<Integer> temp_value = Optional.empty();

        //I don't mind this code anymore, it's alright.
        //what's cool is that it supports as much arguments 
        //as long as the arithmetic is valid
        switch (keyword) {
            case "ADD":
                for (String command_arg : tokenized_commands.subList(2, tokenized_commands.size())) {
                    if (temp_value.isPresent()) {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(temp_value.get() + get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(temp_value.get() + Integer.parseInt(command_arg));
                        }
                    } else {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(Integer.parseInt(command_arg));
                        }
                    }
                }
                break;

            case "SUB":
                for (String command_arg : tokenized_commands.subList(2, tokenized_commands.size())) {
                    if (temp_value.isPresent()) {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(temp_value.get() - get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(temp_value.get() - Integer.parseInt(command_arg));
                        }
                    } else {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(Integer.parseInt(command_arg));
                        }
                    }
                }
                break;

            case "MUL":
                for (String command_arg : tokenized_commands.subList(2, tokenized_commands.size())) {
                    if (temp_value.isPresent()) {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(temp_value.get() * get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(temp_value.get() * Integer.parseInt(command_arg));
                        }
                    } else {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(Integer.parseInt(command_arg));
                        }
                    }
                }
                break;

            case "DIV":
                for (String command_arg : tokenized_commands.subList(2, tokenized_commands.size())) {
                    if (temp_value.isPresent()) {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(temp_value.get() / get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(temp_value.get() / Integer.parseInt(command_arg));
                        }
                    } else {
                        if (is_in_mem(command_arg)) {
                            temp_value = Optional.of(get_variable_value(command_arg));
                        } else {
                            temp_value = Optional.of(Integer.parseInt(command_arg));
                        }
                    }
                }
                break;
        }
        variable_allocator(destination, temp_value.orElse(null));
    }

    //checks if variable is valid syntax
    static void is_valid_var(String var_name) {
        if (!var_name.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid variable name: " + var_name);
        }
    }

    //stores values in hashmap
    static void variable_allocator(String variable_name, int value) {
        is_valid_var(variable_name);

        if (variable_values.containsKey(variable_name)) {
            //if the word is already in the hashmap the value is overwritten
            variable_values.replace(variable_name, value);
        } else {
            variable_values.put(variable_name, value);
        }
    }

    static void source_code_printer() {
        System.out.println(name_of_student);
        System.out.format("%s\n", "*".repeat(46));
        
        Arrays.asList(allocated_memory)
                .stream()
                .filter(line -> line != null)
                .forEach(System.out::println);

       System.out.format("%s\n", "*".repeat(46));
    }

    static void program_decoder() {
        source_code_printer();

        //iterate through memory
        memory_loop: for (String line : allocated_memory) {
            if (line == null) {
                throw new IllegalArgumentException("Allocated Program Wrong");
            }

            //checks for comments and empty lines
            if (line.contains(";") || line.isEmpty()) {
                continue memory_loop;
            }

            //converts the string to actual arrays, allows for easier indexing
            List<String> tokenized_commands = Arrays.asList(line.split(" "));
            keyword_handler(tokenized_commands);
        }
    }

    //prompts user for file path and allocates to memory
    static void program_fetcher() {
        clear_memory();

        //reading file
        System.out.println("Enter the file path: ");
        file_path = user_pending_input.nextLine().trim();
        try {
            user_file = new Scanner(new File(file_path));
        } catch (Exception e) {
            return;
        }

        //allocating onto memory
        for (int mem_counter = 0; user_file.hasNext(); mem_counter++) {
            String line = user_file.nextLine();
            allocated_memory[mem_counter] = line;
        }
    }

    public static void main(String[] args) {
        program_fetcher();
        program_decoder();
    }
}