package com.niklim.clicktrace;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.grapher.GrapherModule;
import com.google.inject.grapher.InjectorGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import com.google.inject.grapher.graphviz.GraphvizRenderer;

public class GuiceGrapher {
	public static void main(String[] args) {
		try {
			graph("dependency-graph.dot", App.createInjector());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void graph(String filename, Injector demoInjector) throws IOException {
		PrintWriter out = new PrintWriter(new File(filename), "UTF-8");

		Injector injector = Guice.createInjector(new GrapherModule(), new GraphvizModule());
		GraphvizRenderer renderer = injector.getInstance(GraphvizRenderer.class);
		renderer.setOut(out).setRankdir("TB");

		injector.getInstance(InjectorGrapher.class).of(demoInjector).graph();
	}
}
