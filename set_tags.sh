BRANCH="master"

# Are we on the right branch?
if [ "$TRAVIS_BRANCH" = "$BRANCH" ]; then
  
  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    
    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
      echo -e "Starting to tag commit.\n"

      git config --global user.email "irakotoseheno@gmail.com"
	  git config --global user.name "eric3475"

	  echo -e "Done git config.\n"

      # Add tag and push to master.
      git tag -a v${TRAVIS_BUILD_NUMBER} -m "Travis build $TRAVIS_BUILD_NUMBER pushed a tag."

      echo -e "Done creating tag.\n"

      git push origin --tags

      echo -e "Done pushing.\n"

      git fetch origin

      echo -e "Done magic with tag.\n"
  fi
  fi
fi