package org.potassco.api;

import java.util.List;

/**
 * Interface that has to be implemented to customize clingo.
 * <p>
 * The following example reproduces the default clingo application:
 * <pre>
    import sys
    import clingo

    class Application:
        def __init__(self, name):
            self.program_name = name

        def main(self, ctl, files):
            if len(files) > 0:
                for f in files:
                    ctl.load(f)
            else:
                ctl.load("-")
            ctl.ground([("base", [])])
            ctl.solve()

    clingo.clingo_main(Application(sys.argv[0]), sys.argv[1:])</pre>
 * @author jschneeberger
 *
 */
public interface Application {
	/**
	 * Optional program name to be used in the help output.
	 */
	public static final String PROGRAM_NAME = "clingo";
	/**
	 * Maximum number of messages passed to the logger.
	 */
	public static final int MESSAGE_LIMIT = 20;

	/**
	 * Function to replace clingo's default main function.
	 * @param control The main control object.
	 * @param files The files passed to clingo_main.
	 */
	public void main(Control control, List<String> files);

	/**
	 * Function to register custom options.
	 * @param options Object to register additional options
	 */
	public void registerOptions(ApplicationOptions options);
	
	/**
	 * Function to validate custom options.
	 * @return false or throw an exception if option validation fails.
	 */
	public boolean validateOptions();
	
	/**
	 * Function to intercept messages normally printed to standard error.
	 * By default, messages are printed to standard error.
	 * This function should not raise exceptions.
	 * @param code The message code.
	 * @param str The message string.
	 */
	public void logger(MessageCode code, String str);

}
