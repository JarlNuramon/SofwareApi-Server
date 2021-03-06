package de.hs.bochum;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.*;
import org.jbehave.core.io.*;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.*;
import org.jbehave.core.steps.ParameterConverters.*;

import static
    org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.*;

public class JBehaveConfig extends JUnitStories {
       
    public JBehaveConfig(){
        configuredEmbedder().embedderControls()
           .doGenerateViewAfterStories(true)
           .doIgnoreFailureInStories(true)
           .doIgnoreFailureInView(true)
           .useThreads(2);
     }

  
    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass 
            = this.getClass();
        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters 
            = new ParameterConverters();
        // add custom converters
        parameterConverters.addConverters(new DateConverter(
            new SimpleDateFormat("yyyy-MM-dd")));
        return new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromClasspath(embeddableClass))
            .useStoryReporterBuilder(new StoryReporterBuilder()
            .withCodeLocation(CodeLocations
            .codeLocationFromClass(embeddableClass))
            .withDefaultFormats()
            .withFormats(CONSOLE, TXT, HTML, XML))
            .useParameterConverters(parameterConverters);
    }
    

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), 
            new MessreiheStartenSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
           .findPaths(codeLocationFromClass(this.getClass()), 
           "**/*.story", "**/excluded*.story");
    }
}

