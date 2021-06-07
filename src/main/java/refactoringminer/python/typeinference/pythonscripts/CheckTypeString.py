from __future__ import print_function

import ast
import json
import textwrap

from pytype import config
from pytype.tools.annotate_ast import annotate_ast

pytype_out_path = '/pytype_output/.pytype/'

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

if __name__ == '__main__':
    module = annotate("xxx={1:'3',1:3}",'test4.imports')
    dic_str = get_annotations_dict(module,0)
    # print(dic_str)
    json_object = json.dumps(dic_str)
    print(json_object)
