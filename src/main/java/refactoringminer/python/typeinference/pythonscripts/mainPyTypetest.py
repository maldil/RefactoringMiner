from __future__ import print_function
import ast
import textwrap
import pytype
from pytype import config
from pytype.tools.annotate_ast import annotate_ast
from typed_ast import ast27 as ast27
from typed_ast import ast3
from pytype.tools.traces import traces
from pytype import file_utils
import sys
import json
import os
import subprocess
import shutil
pytype_out_path = '/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/pytype_output/.pytype/'

def annotate(source,file_name):
  source = textwrap.dedent(source.lstrip('\n'))
  ast_factory = lambda unused_options: ast
 # pytype_options = config.Options.create(python_version=(3,7),nofail=True,protocols=True,no_report_errors=True,keep_going=True,
                          #               imports_map=pytype_out_path+'/imports/'+file_name)
  pytype_options = config.Options.create(python_version=(3,7),nofail=True,protocols=True,
                                         imports_map=pytype_out_path+'/imports/'+file_name)

  module = annotate_ast.annotate_source(source, ast_factory, pytype_options)
  return module

def get_annotations_dict( module,empty_line):
    return [{"lineNumber":_get_node_key(node)[0]+empty_line,"col_offset":_get_node_key(node)[1],"nodeName":_get_node_key(node)[2],"type":node.resolved_annotation}
            for node in ast.walk(module)
            if hasattr(node, 'resolved_type')]

def generate_pytype_folder(project_path,file_path):
  env = os.environ.copy()
  # shutil.rmtree(pytype_out_path)
  subprocess.run(['pytype','--pythonpath='+project_path,'--no-report-errors','--keep-going','--protocols','--output='+pytype_out_path,file_path], shell=False,env=env,cwd=os.path.dirname(os.path.realpath(__file__)))



def _get_node_key(node):
  # AST Name = node.__class__.__name__
  base = (node.lineno,node.col_offset)
  if isinstance(node, ast.Name):
    return base + (node.id,)
  elif isinstance(node, ast.Attribute):
    return base + (node.attr,)
  elif isinstance(node, ast.FunctionDef):
    return base + (node.name,)
  elif isinstance(node, ast.Param):
    return base + (node.name,)
  else:
    return base

def get_ast(options):
  major = options.python_version[0]
  return {2: ast27, 3: ast3}[major]

def main1():
  if len(sys.argv)>1:
    file_path = sys.argv[1]
  else:
    file_path = '/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner/django-debug-toolbar/tests/settings.py'

  if len(sys.argv)>2:
    project_name = sys.argv[2]
  else:
    project_name = '/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner/django-debug-toolbar'

  if len(sys.argv)>3:
    file_name = sys.argv[3]
  else:
    file_name = 'tests.settings..py'



  generate_pytype_folder(project_path=project_name,file_path=file_path)

  with open(file_path, 'r') as f:
    src = f.read()

  first_empty_count=0
  for line in src.split('\n'):
    if len(line)==0:
      first_empty_count +=1
    else:
      break

  print(file_name[:-3]+'imports')

  module = annotate(src,file_name[:-3]+'imports')
  dic_str = get_annotations_dict(module,first_empty_count)
  # print(dic_str)
  json_object = json.dumps(dic_str)

  print(json_object)

if __name__ == '__main__':
  main1()

