#!/usr/bin/env python


import glob
import re
#import csv
#from Bio.UniProt import GOA
#import obitools.goa.parser


# Input directory path
#mydir = '/home/venkatesan/workspace/explore/test_files/po_association/*.*'
mydir = '/home/venkatesan/workspace/explore/test_files/go_association/go_gene_association/*.*'
files = glob.glob(mydir) # stores file names(with the full path) as a list

# Output file path - RDF files - turtle syntax 
#output_path = '/home/venkatesan/workspace/explore/output/output.txt'
#output_file = open(output_path, "w") 

my_file = '/home/venkatesan/Documents/data_samples/ECO_IDs_terms_definitions.txt'


eco_field = [
             'Eco_ID',
             'Eco_Term',
             'Description',
             'Code'
             ]



handle = open(my_file, "r")
lines = handle.readlines()
for line in lines:
    records = line.split("\t")
    if records[3]:
        print records[3]
#    for record in records:
#        print record
#gaffields = header.replace('!', '')
#for field in gaffields:
#    print field + "\n"
#print gaffields
#for inline in lines:
#    inrec = inline.r
           
handle.close()
#output_file.close()    