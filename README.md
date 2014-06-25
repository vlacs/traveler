# Traveler ![Build Status](https://codeship.io/projects/84d9a4a0-c8a0-0131-554f-3aa2000f7504/status) [![Coverage](http://img.shields.io/coveralls/vlacs/traveler/master.svg)](http://coveralls.io/r/vlacs/traveler)

Traveler stores VLACS' users in our datomic database and provides a user interface to search users and change passwords.

## Running Tests
You can build the necessary dependencies and run the tests with one command.

```$ sh test.sh```

## Running the Traveler project

If you'd like to run Traveler as a standalone application just complete the following steps:

```
$ git clone https://github.com/vlacs/traveler
$ cd traveler
$ lein voom freshen
$ lein voom build-deps
$ lein cljsbuild once
$ lein immutant server
```

Once the Immutant server is up you can navigate to ''' http://localhost:8080 ''' in your browser.

If you'd like to connect to Traveler's REPL do the following inside of the project directory:

``` $ lein repl :connect ```

## Artifact

* Releases are published to [Clojars](http://clojars.org/org.vlacs/traveler "Traveler - Clojars")

Latest Artifact: ``` [org.vlacs/traveler "0.2.0"] ```

## Adding users

To add users to the Taveler section of the datomic database Gangway should call
the traveler.api.user/add-user fn.

add-user takes a map with the following format:

```
{:id-sk                "1239048"
 :username             "mgeorge"
 :password             "d783andf094nadsfHASHED09anPASSWORD"
 :privilege            "ACTIVE"
 :firstname            "Mike"
 :lastname             "George"
 :email                "mgeorge@vlacs.org"
 :policies-assent-date "12/01/2013"
 :istest               true
 :can-masquerade       true}
```
```:istest``` and ```:can-masquerade``` are optional and default to false.

## Copyright and License

Created by [Mike George](http://www.mikegeorge.org)

Copyright © 2014 Virtual Learning Academy Charter School

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

<p align="center"><a href="http://vlacs.org/" target="_blank"><img src="http://vlacs.org/images/VLACS_logo_no_dep_website.png" alt="VLACS Logo"/></a></p>
