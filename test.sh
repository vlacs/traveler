#!/bin/bash
lein do voom freshen, voom build-deps, cljsbuild once, immutant test
