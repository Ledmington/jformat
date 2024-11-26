package com.ledmington.javaparser.bench;

import java.util.concurrent.TimeUnit;

import com.ledmington.javaparser.parser.JavaParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@BenchmarkMode({ Mode.SampleTime })
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 5)
public class Parsing {

	private static final String code = String.join(
			"\n",
			"package org.testing;",
			"import java.util.List;",
			"import java.util.ArrayList;",
			"private abstract class A extends Object implements Interface1,Interface2{",
			"int x=0;",
			"private float y;",
			"private void banana(){return 1+x+2;}",
			"public static final List<Map<String,List<Integer>>> myList=(3*5)+(7/(9-11));",
			"static abstract int mymethod();",
			"void m(int x, final char[] y){a();long z=\"ciao\";if('a'!=1.5f){}else{a.b[5].c(17,p);}}");;

	@Benchmark
	public void parse(final Blackhole bh) {
		bh.consume(JavaParser.parse(code));
	}
}