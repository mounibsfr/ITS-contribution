package fr.lip6.move.gal.ltsmin.launch;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import fr.lip6.move.gal.flatten.popup.actions.ConsoleAdder;
import fr.lip6.move.gal.process.CommandLine;


public class ITSLaunchDelegate extends LaunchConfigurationDelegate implements
ILaunchConfigurationDelegate2 {


	@Override
	public void launch(ILaunchConfiguration configuration, String mode,	ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ConsoleAdder.startConsole();

		List<CommandLine> cls = CommandLineBuilder.buildCommand(configuration);
		
		// Bring it all together for the invocation
		// full argument list		
		Logger.getLogger("fr.lip6.move.gal").info("Running command line " + cls);

		for (CommandLine cl : cls) {
			
			// Define the process
			Process p = DebugPlugin.exec(cl.getArgs(), cl.getWorkingDir().getAbsoluteFile() );
			try {
				p.waitFor();
			} catch (InterruptedException e) {					
				e.printStackTrace();
			}

			// Let the DebugPlugin manage running the process
			IProcess proc = DebugPlugin.newProcess(launch, p, "LTSmin runner");
			
			// System.out.println("done!");
		}		
	}

}
