#!/bin/sh

./gradlew findbugs

RESULT=$?

if [ $RESULT -eq 1 ]
	then
		echo --------------------------------------------
		echo PUSH REJECTED BECAUSE YOU HAVE LINTER ERROR.
		echo --------------------------------------------
fi

exit $RESULT
