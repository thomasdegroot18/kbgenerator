from urllib.parse import urlparse
import urllib.parse


def replaceCharacter(url):
    _invalid_uri_chars = '<>" {}|\\^`'
    for elem in _invalid_uri_chars:
        url = url.replace(elem, "")
    return url


def cleanOBJECT(BADurl):
    elem = BADurl.split("^^<")
    if len(elem) > 1:
        return elem[0]+"^^<"+replaceCharacter(urllib.parse.quote((elem[1])))+">"
    else:
        return "<"+replaceCharacter(BADurl)+">"


def is_object(object):
    elem = object.split("^^<")
    _invalid_uri_chars = '<>" {}|\\^`'

    if len(elem) > 1:
        for char in _invalid_uri_chars:
            if char in elem[1]:
                return False
        return is_url(elem[1])
    else:
        for char in _invalid_uri_chars:
            if char in object:
                return False
        return is_url(object)


def is_url(url):
    _invalid_uri_chars = '<>" {}|\\^`'
    try:
        result = urlparse(url)
        for char in _invalid_uri_chars:
            if char in url:
                return False
        return all([result.scheme, result.netloc])
    except ValueError:
        return False


def cleaner(line):

    linelist = line.split(">")
    subject = linelist[0].split("<")[1]
    pred = linelist[1].split(" <")[1]
    object = linelist[2][2:]

    if is_url(subject) and is_url(pred) and is_object(object):
        if line.count("<") == 3 and line.count(">") == 3 and line.count(" .") == 1:
            return line
        else:
            return False

    if not(is_url(subject)):
        subject = replaceCharacter(subject)
    if not(is_url(pred)):
        pred = replaceCharacter(pred)

    if not(is_object(object)):
        object = cleanOBJECT(object)
    else:
        object = "<"+object+">"
    line = "<"+subject+"> <"+pred+"> "+object+" ."
    if line.count("<") == 3 and line.count(">") == 3 and line.count(" .") == 1:
        return line
    else:
        return False


def main():
    inputFile = "Sample-dbpedia2016-04en"
    inputFile = "Sample-dblp-2012-11-28b"
    outputFile = inputFile+"-clean.nt"
    EOF = False
    with open(inputFile+".nt", "r") as input:
        with open(outputFile, "w+") as output:
            while not(input.closed):
                lineInput = input.readline()
                if len(lineInput) == 0:
                    break
                else:
                    CleanedLine = cleaner(lineInput)
                    if CleanedLine == False:
                        if EOF == True:
                            break
                        EOF = True
                        pass
                    else:
                        output.write(CleanedLine)


main()
