#!/usr/bin/env bash
source variables.sh


source_dir1="inputData$1"
source_dir2="inputData$2"
rm -rf "$source_dir1"
rm -rf "$source_dir2"
mkdir -p "$source_dir1"
mkdir -p "$source_dir2"
input_files="$(ls -1 *.tar.gz) $(ls -1 *.tgz)"

for file in $input_files; do
  echo "cp $file $source_dir1"
  cp "$file" "$source_dir1"
  echo "cp $file $source_dir2"
  cp "$file" "$source_dir2"
  echo "(cd $source_dir1; tar xvzf $file)"
  (cd "$source_dir1"; tar xvzf "$file"; find . -mindepth 2 -type f -exec mv -f '{}' . ';'; find . -type d -delete)
   echo "(cd $source_dir2; tar xvzf $file)"
  (cd "$source_dir2"; tar xvzf "$file"; find . -mindepth 2 -type f -exec mv -f '{}' . ';'; find . -type d -delete)
  echo "rm $source_dir1/$file"
  rm "$source_dir1"/"$file"
  echo "rm $source_dir2/$file"
  rm "$source_dir2"/"$file"

  #rand=$(cat /dev/urandom | tr -dc A-Za-z0-9 | head -c 10)
  rm "$file"
done

