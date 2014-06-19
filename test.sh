#!/bin/bash
lein voom freshen
lein voom build-deps
lein cljsbuild once
lein immutant test
