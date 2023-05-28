/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tk.mybatis.mapper.util;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a somewhat comprehensive list of SQL reserved words.
 * Since different databases have different reserved words, this list is
 * inclusive of many different databases - so it may include words that are not
 * reserved in some databases.
 *
 * <p>This list is based on the list from Drupal Handbook:
 * http://drupal.org/node/141051 With additions for DB2
 *
 * @author Jeff Butler
 */
public class SqlReservedWords {

    private static Set<String> RESERVED_WORDS;

    static {
        String[] words = { //$NON-NLS-1$
        "A", //$NON-NLS-1$
        "ABORT", //$NON-NLS-1$
        "ABS", //$NON-NLS-1$
        "ABSOLUTE", //$NON-NLS-1$
        "ACCESS", //$NON-NLS-1$
        "ACTION", //$NON-NLS-1$
        "ADA", // DB2 //$NON-NLS-1$
        "ADD", //$NON-NLS-1$
        "ADMIN", // DB2 //$NON-NLS-1$
        "AFTER", //$NON-NLS-1$
        "AGGREGATE", // DB2 //$NON-NLS-1$
        "ALIAS", // DB2 //$NON-NLS-1$
        "ALL", // DB2 //$NON-NLS-1$
        "ALLOCATE", // DB2 //$NON-NLS-1$
        "ALLOW", //$NON-NLS-1$
        "ALSO", // DB2 //$NON-NLS-1$
        "ALTER", //$NON-NLS-1$
        "ALWAYS", //$NON-NLS-1$
        "ANALYSE", //$NON-NLS-1$
        "ANALYZE", // DB2 //$NON-NLS-1$
        "AND", // DB2 //$NON-NLS-1$
        "ANY", // DB2 //$NON-NLS-1$
        "APPLICATION", //$NON-NLS-1$
        "ARE", //$NON-NLS-1$
        "ARRAY", // DB2 //$NON-NLS-1$
        "AS", //$NON-NLS-1$
        "ASC", //$NON-NLS-1$
        "ASENSITIVE", //$NON-NLS-1$
        "ASSERTION", //$NON-NLS-1$
        "ASSIGNMENT", // DB2 //$NON-NLS-1$
        "ASSOCIATE", // DB2 //$NON-NLS-1$
        "ASUTIME", //$NON-NLS-1$
        "ASYMMETRIC", //$NON-NLS-1$
        "AT", //$NON-NLS-1$
        "ATOMIC", //$NON-NLS-1$
        "ATTRIBUTE", //$NON-NLS-1$
        "ATTRIBUTES", // DB2 //$NON-NLS-1$
        "AUDIT", // DB2 //$NON-NLS-1$
        "AUTHORIZATION", //$NON-NLS-1$
        "AUTO_INCREMENT", // DB2 //$NON-NLS-1$
        "AUX", // DB2 //$NON-NLS-1$
        "AUXILIARY", //$NON-NLS-1$
        "AVG", //$NON-NLS-1$
        "AVG_ROW_LENGTH", //$NON-NLS-1$
        "BACKUP", //$NON-NLS-1$
        "BACKWARD", // DB2 //$NON-NLS-1$
        "BEFORE", // DB2 //$NON-NLS-1$
        "BEGIN", //$NON-NLS-1$
        "BERNOULLI", // DB2 //$NON-NLS-1$
        "BETWEEN", //$NON-NLS-1$
        "BIGINT", // DB2 //$NON-NLS-1$
        "BINARY", //$NON-NLS-1$
        "BIT", //$NON-NLS-1$
        "BIT_LENGTH", //$NON-NLS-1$
        "BITVAR", //$NON-NLS-1$
        "BLOB", //$NON-NLS-1$
        "BOOL", //$NON-NLS-1$
        "BOOLEAN", //$NON-NLS-1$
        "BOTH", //$NON-NLS-1$
        "BREADTH", //$NON-NLS-1$
        "BREAK", //$NON-NLS-1$
        "BROWSE", // DB2 //$NON-NLS-1$
        "BUFFERPOOL", //$NON-NLS-1$
        "BULK", // DB2 //$NON-NLS-1$
        "BY", //$NON-NLS-1$
        "C", // DB2 //$NON-NLS-1$
        "CACHE", // DB2 //$NON-NLS-1$
        "CALL", // DB2 //$NON-NLS-1$
        "CALLED", // DB2 //$NON-NLS-1$
        "CAPTURE", // DB2 //$NON-NLS-1$
        "CARDINALITY", //$NON-NLS-1$
        "CASCADE", // DB2 //$NON-NLS-1$
        "CASCADED", // DB2 //$NON-NLS-1$
        "CASE", // DB2 //$NON-NLS-1$
        "CAST", //$NON-NLS-1$
        "CATALOG", //$NON-NLS-1$
        "CATALOG_NAME", // DB2 //$NON-NLS-1$
        "CCSID", //$NON-NLS-1$
        "CEIL", //$NON-NLS-1$
        "CEILING", //$NON-NLS-1$
        "CHAIN", //$NON-NLS-1$
        "CHANGE", // DB2 //$NON-NLS-1$
        "CHAR", //$NON-NLS-1$
        "CHAR_LENGTH", // DB2 //$NON-NLS-1$
        "CHARACTER", //$NON-NLS-1$
        "CHARACTER_LENGTH", //$NON-NLS-1$
        "CHARACTER_SET_CATALOG", //$NON-NLS-1$
        "CHARACTER_SET_NAME", //$NON-NLS-1$
        "CHARACTER_SET_SCHEMA", //$NON-NLS-1$
        "CHARACTERISTICS", //$NON-NLS-1$
        "CHARACTERS", // DB2 //$NON-NLS-1$
        "CHECK", //$NON-NLS-1$
        "CHECKED", //$NON-NLS-1$
        "CHECKPOINT", //$NON-NLS-1$
        "CHECKSUM", //$NON-NLS-1$
        "CLASS", //$NON-NLS-1$
        "CLASS_ORIGIN", //$NON-NLS-1$
        "CLOB", // DB2 //$NON-NLS-1$
        "CLOSE", // DB2 //$NON-NLS-1$
        "CLUSTER", //$NON-NLS-1$
        "CLUSTERED", //$NON-NLS-1$
        "COALESCE", //$NON-NLS-1$
        "COBOL", //$NON-NLS-1$
        "COLLATE", //$NON-NLS-1$
        "COLLATION", //$NON-NLS-1$
        "COLLATION_CATALOG", //$NON-NLS-1$
        "COLLATION_NAME", //$NON-NLS-1$
        "COLLATION_SCHEMA", //$NON-NLS-1$
        "COLLECT", // DB2 //$NON-NLS-1$
        "COLLECTION", // DB2 //$NON-NLS-1$
        "COLLID", // DB2 //$NON-NLS-1$
        "COLUMN", //$NON-NLS-1$
        "COLUMN_NAME", //$NON-NLS-1$
        "COLUMNS", //$NON-NLS-1$
        "COMMAND_FUNCTION", //$NON-NLS-1$
        "COMMAND_FUNCTION_CODE", // DB2 //$NON-NLS-1$
        "COMMENT", // DB2 //$NON-NLS-1$
        "COMMIT", //$NON-NLS-1$
        "COMMITTED", //$NON-NLS-1$
        "COMPLETION", //$NON-NLS-1$
        "COMPRESS", //$NON-NLS-1$
        "COMPUTE", // DB2 //$NON-NLS-1$
        "CONCAT", // DB2 //$NON-NLS-1$
        "CONDITION", //$NON-NLS-1$
        "CONDITION_NUMBER", // DB2 //$NON-NLS-1$
        "CONNECT", // DB2 //$NON-NLS-1$
        "CONNECTION", //$NON-NLS-1$
        "CONNECTION_NAME", // DB2 //$NON-NLS-1$
        "CONSTRAINT", //$NON-NLS-1$
        "CONSTRAINT_CATALOG", //$NON-NLS-1$
        "CONSTRAINT_NAME", //$NON-NLS-1$
        "CONSTRAINT_SCHEMA", //$NON-NLS-1$
        "CONSTRAINTS", //$NON-NLS-1$
        "CONSTRUCTOR", // DB2 //$NON-NLS-1$
        "CONTAINS", //$NON-NLS-1$
        "CONTAINSTABLE", // DB2 //$NON-NLS-1$
        "CONTINUE", //$NON-NLS-1$
        "CONVERSION", //$NON-NLS-1$
        "CONVERT", //$NON-NLS-1$
        "COPY", //$NON-NLS-1$
        "CORR", //$NON-NLS-1$
        "CORRESPONDING", // DB2 //$NON-NLS-1$
        "COUNT", // DB2 //$NON-NLS-1$
        "COUNT_BIG", //$NON-NLS-1$
        "COVAR_POP", //$NON-NLS-1$
        "COVAR_SAMP", // DB2 //$NON-NLS-1$
        "CREATE", //$NON-NLS-1$
        "CREATEDB", //$NON-NLS-1$
        "CREATEROLE", //$NON-NLS-1$
        "CREATEUSER", // DB2 //$NON-NLS-1$
        "CROSS", //$NON-NLS-1$
        "CSV", //$NON-NLS-1$
        "CUBE", //$NON-NLS-1$
        "CUME_DIST", // DB2 //$NON-NLS-1$
        "CURRENT", // DB2 //$NON-NLS-1$
        "CURRENT_DATE", //$NON-NLS-1$
        "CURRENT_DEFAULT_TRANSFORM_GROUP", // DB2 //$NON-NLS-1$
        "CURRENT_LC_CTYPE", // DB2 //$NON-NLS-1$
        "CURRENT_PATH", //$NON-NLS-1$
        "CURRENT_ROLE", // DB2 //$NON-NLS-1$
        "CURRENT_SERVER", // DB2 //$NON-NLS-1$
        "CURRENT_TIME", // DB2 //$NON-NLS-1$
        "CURRENT_TIMESTAMP", // DB2 //$NON-NLS-1$
        "CURRENT_TIMEZONE", //$NON-NLS-1$
        "CURRENT_TRANSFORM_GROUP_FOR_TYPE", // DB2 //$NON-NLS-1$
        "CURRENT_USER", // DB2 //$NON-NLS-1$
        "CURSOR", //$NON-NLS-1$
        "CURSOR_NAME", // DB2 //$NON-NLS-1$
        "CYCLE", // DB2 //$NON-NLS-1$
        "DATA", // DB2 //$NON-NLS-1$
        "DATABASE", //$NON-NLS-1$
        "DATABASES", //$NON-NLS-1$
        "DATE", //$NON-NLS-1$
        "DATETIME", //$NON-NLS-1$
        "DATETIME_INTERVAL_CODE", //$NON-NLS-1$
        "DATETIME_INTERVAL_PRECISION", // DB2 //$NON-NLS-1$
        "DAY", //$NON-NLS-1$
        "DAY_HOUR", //$NON-NLS-1$
        "DAY_MICROSECOND", //$NON-NLS-1$
        "DAY_MINUTE", //$NON-NLS-1$
        "DAY_SECOND", //$NON-NLS-1$
        "DAYOFMONTH", //$NON-NLS-1$
        "DAYOFWEEK", //$NON-NLS-1$
        "DAYOFYEAR", // DB2 //$NON-NLS-1$
        "DAYS", // DB2 //$NON-NLS-1$
        "DB2GENERAL", // DB2 //$NON-NLS-1$
        "DB2GNRL", // DB2 //$NON-NLS-1$
        "DB2SQL", //$NON-NLS-1$
        "DBCC", // DB2 //$NON-NLS-1$
        "DBINFO", //$NON-NLS-1$
        "DEALLOCATE", //$NON-NLS-1$
        "DEC", //$NON-NLS-1$
        "DECIMAL", // DB2 //$NON-NLS-1$
        "DECLARE", // DB2 //$NON-NLS-1$
        "DEFAULT", // DB2 //$NON-NLS-1$
        "DEFAULTS", //$NON-NLS-1$
        "DEFERRABLE", //$NON-NLS-1$
        "DEFERRED", //$NON-NLS-1$
        "DEFINED", //$NON-NLS-1$
        "DEFINER", // DB2 //$NON-NLS-1$
        "DEFINITION", //$NON-NLS-1$
        "DEGREE", //$NON-NLS-1$
        "DELAY_KEY_WRITE", //$NON-NLS-1$
        "DELAYED", // DB2 //$NON-NLS-1$
        "DELETE", //$NON-NLS-1$
        "DELIMITER", //$NON-NLS-1$
        "DELIMITERS", //$NON-NLS-1$
        "DENSE_RANK", //$NON-NLS-1$
        "DENY", //$NON-NLS-1$
        "DEPTH", //$NON-NLS-1$
        "DEREF", //$NON-NLS-1$
        "DERIVED", //$NON-NLS-1$
        "DESC", //$NON-NLS-1$
        "DESCRIBE", // DB2 //$NON-NLS-1$
        "DESCRIPTOR", //$NON-NLS-1$
        "DESTROY", //$NON-NLS-1$
        "DESTRUCTOR", // DB2 //$NON-NLS-1$
        "DETERMINISTIC", //$NON-NLS-1$
        "DIAGNOSTICS", //$NON-NLS-1$
        "DICTIONARY", //$NON-NLS-1$
        "DISABLE", // DB2 //$NON-NLS-1$
        "DISALLOW", // DB2 //$NON-NLS-1$
        "DISCONNECT", //$NON-NLS-1$
        "DISK", //$NON-NLS-1$
        "DISPATCH", // DB2 //$NON-NLS-1$
        "DISTINCT", //$NON-NLS-1$
        "DISTINCTROW", //$NON-NLS-1$
        "DISTRIBUTED", //$NON-NLS-1$
        "DIV", // DB2 //$NON-NLS-1$
        "DO", //$NON-NLS-1$
        "DOMAIN", // DB2 //$NON-NLS-1$
        "DOUBLE", // DB2 //$NON-NLS-1$
        "DROP", // DB2 //$NON-NLS-1$
        "DSNHATTR", // DB2 //$NON-NLS-1$
        "DSSIZE", //$NON-NLS-1$
        "DUAL", //$NON-NLS-1$
        "DUMMY", //$NON-NLS-1$
        "DUMP", // DB2 //$NON-NLS-1$
        "DYNAMIC", //$NON-NLS-1$
        "DYNAMIC_FUNCTION", //$NON-NLS-1$
        "DYNAMIC_FUNCTION_CODE", // DB2 //$NON-NLS-1$
        "EACH", // DB2 //$NON-NLS-1$
        "EDITPROC", //$NON-NLS-1$
        "ELEMENT", // DB2 //$NON-NLS-1$
        "ELSE", // DB2 //$NON-NLS-1$
        "ELSEIF", //$NON-NLS-1$
        "ENABLE", //$NON-NLS-1$
        "ENCLOSED", // DB2 //$NON-NLS-1$
        "ENCODING", //$NON-NLS-1$
        "ENCRYPTED", // DB2 //$NON-NLS-1$
        "END", // DB2 //$NON-NLS-1$
        "END-EXEC", // DB2 //$NON-NLS-1$
        "END-EXEC1", //$NON-NLS-1$
        "ENUM", //$NON-NLS-1$
        "EQUALS", // DB2 //$NON-NLS-1$
        "ERASE", //$NON-NLS-1$
        "ERRLVL", // DB2 //$NON-NLS-1$
        "ESCAPE", //$NON-NLS-1$
        "ESCAPED", //$NON-NLS-1$
        "EVERY", // DB2 //$NON-NLS-1$
        "EXCEPT", // DB2 //$NON-NLS-1$
        "EXCEPTION", //$NON-NLS-1$
        "EXCLUDE", // DB2 //$NON-NLS-1$
        "EXCLUDING", //$NON-NLS-1$
        "EXCLUSIVE", //$NON-NLS-1$
        "EXEC", // DB2 //$NON-NLS-1$
        "EXECUTE", //$NON-NLS-1$
        "EXISTING", // DB2 //$NON-NLS-1$
        "EXISTS", // DB2 //$NON-NLS-1$
        "EXIT", //$NON-NLS-1$
        "EXP", //$NON-NLS-1$
        "EXPLAIN", // DB2 //$NON-NLS-1$
        "EXTERNAL", //$NON-NLS-1$
        "EXTRACT", //$NON-NLS-1$
        "FALSE", // DB2 //$NON-NLS-1$
        "FENCED", // DB2 //$NON-NLS-1$
        "FETCH", // DB2 //$NON-NLS-1$
        "FIELDPROC", //$NON-NLS-1$
        "FIELDS", // DB2 //$NON-NLS-1$
        "FILE", //$NON-NLS-1$
        "FILLFACTOR", //$NON-NLS-1$
        "FILTER", // DB2 //$NON-NLS-1$
        "FINAL", //$NON-NLS-1$
        "FIRST", //$NON-NLS-1$
        "FLOAT", //$NON-NLS-1$
        "FLOAT4", //$NON-NLS-1$
        "FLOAT8", //$NON-NLS-1$
        "FLOOR", //$NON-NLS-1$
        "FLUSH", //$NON-NLS-1$
        "FOLLOWING", // DB2 //$NON-NLS-1$
        "FOR", //$NON-NLS-1$
        "FORCE", // DB2 //$NON-NLS-1$
        "FOREIGN", //$NON-NLS-1$
        "FORTRAN", //$NON-NLS-1$
        "FORWARD", //$NON-NLS-1$
        "FOUND", // DB2 //$NON-NLS-1$
        "FREE", //$NON-NLS-1$
        "FREETEXT", //$NON-NLS-1$
        "FREETEXTTABLE", //$NON-NLS-1$
        "FREEZE", // DB2 //$NON-NLS-1$
        "FROM", // DB2 //$NON-NLS-1$
        "FULL", //$NON-NLS-1$
        "FULLTEXT", // DB2 //$NON-NLS-1$
        "FUNCTION", //$NON-NLS-1$
        "FUSION", //$NON-NLS-1$
        "G", // DB2 //$NON-NLS-1$
        "GENERAL", // DB2 //$NON-NLS-1$
        "GENERATED", // DB2 //$NON-NLS-1$
        "GET", // DB2 //$NON-NLS-1$
        "GLOBAL", // DB2 //$NON-NLS-1$
        "GO", // DB2 //$NON-NLS-1$
        "GOTO", // DB2 //$NON-NLS-1$
        "GRANT", //$NON-NLS-1$
        "GRANTED", //$NON-NLS-1$
        "GRANTS", // DB2 //$NON-NLS-1$
        "GRAPHIC", //$NON-NLS-1$
        "GREATEST", // DB2 //$NON-NLS-1$
        "GROUP", //$NON-NLS-1$
        "GROUPING", // DB2 //$NON-NLS-1$
        "HANDLER", // DB2 //$NON-NLS-1$
        "HAVING", //$NON-NLS-1$
        "HEADER", //$NON-NLS-1$
        "HEAP", //$NON-NLS-1$
        "HIERARCHY", //$NON-NLS-1$
        "HIGH_PRIORITY", // DB2 //$NON-NLS-1$
        "HOLD", //$NON-NLS-1$
        "HOLDLOCK", //$NON-NLS-1$
        "HOST", //$NON-NLS-1$
        "HOSTS", // DB2 //$NON-NLS-1$
        "HOUR", //$NON-NLS-1$
        "HOUR_MICROSECOND", //$NON-NLS-1$
        "HOUR_MINUTE", //$NON-NLS-1$
        "HOUR_SECOND", // DB2 //$NON-NLS-1$
        "HOURS", //$NON-NLS-1$
        "IDENTIFIED", // DB2 //$NON-NLS-1$
        "IDENTITY", //$NON-NLS-1$
        "IDENTITY_INSERT", //$NON-NLS-1$
        "IDENTITYCOL", // DB2 //$NON-NLS-1$
        "IF", //$NON-NLS-1$
        "IGNORE", //$NON-NLS-1$
        "ILIKE", // DB2 //$NON-NLS-1$
        "IMMEDIATE", //$NON-NLS-1$
        "IMMUTABLE", //$NON-NLS-1$
        "IMPLEMENTATION", //$NON-NLS-1$
        "IMPLICIT", // DB2 //$NON-NLS-1$
        "IN", //$NON-NLS-1$
        "INCLUDE", // DB2 //$NON-NLS-1$
        "INCLUDING", // DB2 //$NON-NLS-1$
        "INCREMENT", // DB2 //$NON-NLS-1$
        "INDEX", // DB2 //$NON-NLS-1$
        "INDICATOR", //$NON-NLS-1$
        "INFILE", //$NON-NLS-1$
        "INFIX", // DB2 //$NON-NLS-1$
        "INHERIT", //$NON-NLS-1$
        "INHERITS", //$NON-NLS-1$
        "INITIAL", //$NON-NLS-1$
        "INITIALIZE", //$NON-NLS-1$
        "INITIALLY", // DB2 //$NON-NLS-1$
        "INNER", // DB2 //$NON-NLS-1$
        "INOUT", //$NON-NLS-1$
        "INPUT", // DB2 //$NON-NLS-1$
        "INSENSITIVE", // DB2 //$NON-NLS-1$
        "INSERT", //$NON-NLS-1$
        "INSERT_ID", //$NON-NLS-1$
        "INSTANCE", //$NON-NLS-1$
        "INSTANTIABLE", //$NON-NLS-1$
        "INSTEAD", //$NON-NLS-1$
        "INT", //$NON-NLS-1$
        "INT1", //$NON-NLS-1$
        "INT2", //$NON-NLS-1$
        "INT3", //$NON-NLS-1$
        "INT4", //$NON-NLS-1$
        "INT8", //$NON-NLS-1$
        "INTEGER", // DB2 //$NON-NLS-1$
        "INTEGRITY", //$NON-NLS-1$
        "INTERSECT", //$NON-NLS-1$
        "INTERSECTION", //$NON-NLS-1$
        "INTERVAL", // DB2 //$NON-NLS-1$
        "INTO", //$NON-NLS-1$
        "INVOKER", // DB2 //$NON-NLS-1$
        "IS", //$NON-NLS-1$
        "ISAM", //$NON-NLS-1$
        "ISNULL", // DB2 //$NON-NLS-1$
        "ISOBID", // DB2 //$NON-NLS-1$
        "ISOLATION", // DB2 //$NON-NLS-1$
        "ITERATE", // DB2 //$NON-NLS-1$
        "JAR", // DB2 //$NON-NLS-1$
        "JAVA", // DB2 //$NON-NLS-1$
        "JOIN", //$NON-NLS-1$
        "K", // DB2 //$NON-NLS-1$
        "KEY", //$NON-NLS-1$
        "KEY_MEMBER", //$NON-NLS-1$
        "KEY_TYPE", //$NON-NLS-1$
        "KEYS", //$NON-NLS-1$
        "KILL", // DB2 //$NON-NLS-1$
        "LABEL", //$NON-NLS-1$
        "LANCOMPILER", // DB2 //$NON-NLS-1$
        "LANGUAGE", //$NON-NLS-1$
        "LARGE", //$NON-NLS-1$
        "LAST", //$NON-NLS-1$
        "LAST_INSERT_ID", //$NON-NLS-1$
        "LATERAL", // DB2 //$NON-NLS-1$
        "LC_CTYPE", //$NON-NLS-1$
        "LEADING", //$NON-NLS-1$
        "LEAST", // DB2 //$NON-NLS-1$
        "LEAVE", // DB2 //$NON-NLS-1$
        "LEFT", //$NON-NLS-1$
        "LENGTH", //$NON-NLS-1$
        "LESS", //$NON-NLS-1$
        "LEVEL", // DB2 //$NON-NLS-1$
        "LIKE", //$NON-NLS-1$
        "LIMIT", //$NON-NLS-1$
        "LINENO", //$NON-NLS-1$
        "LINES", // DB2 //$NON-NLS-1$
        "LINKTYPE", //$NON-NLS-1$
        "LISTEN", //$NON-NLS-1$
        "LN", //$NON-NLS-1$
        "LOAD", // DB2 //$NON-NLS-1$
        "LOCAL", // DB2 //$NON-NLS-1$
        "LOCALE", //$NON-NLS-1$
        "LOCALTIME", //$NON-NLS-1$
        "LOCALTIMESTAMP", //$NON-NLS-1$
        "LOCATION", // DB2 //$NON-NLS-1$
        "LOCATOR", // DB2 //$NON-NLS-1$
        "LOCATORS", // DB2 //$NON-NLS-1$
        "LOCK", // DB2 //$NON-NLS-1$
        "LOCKMAX", // DB2 //$NON-NLS-1$
        "LOCKSIZE", //$NON-NLS-1$
        "LOGIN", //$NON-NLS-1$
        "LOGS", // DB2 //$NON-NLS-1$
        "LONG", //$NON-NLS-1$
        "LONGBLOB", //$NON-NLS-1$
        "LONGTEXT", // DB2 //$NON-NLS-1$
        "LOOP", //$NON-NLS-1$
        "LOW_PRIORITY", //$NON-NLS-1$
        "LOWER", //$NON-NLS-1$
        "M", //$NON-NLS-1$
        "MAP", //$NON-NLS-1$
        "MATCH", //$NON-NLS-1$
        "MATCHED", //$NON-NLS-1$
        "MAX", //$NON-NLS-1$
        "MAX_ROWS", //$NON-NLS-1$
        "MAXEXTENTS", // DB2 //$NON-NLS-1$
        "MAXVALUE", //$NON-NLS-1$
        "MEDIUMBLOB", //$NON-NLS-1$
        "MEDIUMINT", //$NON-NLS-1$
        "MEDIUMTEXT", //$NON-NLS-1$
        "MEMBER", //$NON-NLS-1$
        "MERGE", //$NON-NLS-1$
        "MESSAGE_LENGTH", //$NON-NLS-1$
        "MESSAGE_OCTET_LENGTH", //$NON-NLS-1$
        "MESSAGE_TEXT", //$NON-NLS-1$
        "METHOD", // DB2 //$NON-NLS-1$
        "MICROSECOND", // DB2 //$NON-NLS-1$
        "MICROSECONDS", //$NON-NLS-1$
        "MIDDLEINT", //$NON-NLS-1$
        "MIN", //$NON-NLS-1$
        "MIN_ROWS", //$NON-NLS-1$
        "MINUS", // DB2 //$NON-NLS-1$
        "MINUTE", //$NON-NLS-1$
        "MINUTE_MICROSECOND", //$NON-NLS-1$
        "MINUTE_SECOND", // DB2 //$NON-NLS-1$
        "MINUTES", // DB2 //$NON-NLS-1$
        "MINVALUE", //$NON-NLS-1$
        "MLSLABEL", //$NON-NLS-1$
        "MOD", // DB2 //$NON-NLS-1$
        "MODE", // DB2 //$NON-NLS-1$
        "MODIFIES", //$NON-NLS-1$
        "MODIFY", //$NON-NLS-1$
        "MODULE", // DB2 //$NON-NLS-1$
        "MONTH", //$NON-NLS-1$
        "MONTHNAME", // DB2 //$NON-NLS-1$
        "MONTHS", //$NON-NLS-1$
        "MORE", //$NON-NLS-1$
        "MOVE", //$NON-NLS-1$
        "MULTISET", //$NON-NLS-1$
        "MUMPS", //$NON-NLS-1$
        "MYISAM", //$NON-NLS-1$
        "NAME", //$NON-NLS-1$
        "NAMES", //$NON-NLS-1$
        "NATIONAL", //$NON-NLS-1$
        "NATURAL", //$NON-NLS-1$
        "NCHAR", //$NON-NLS-1$
        "NCLOB", //$NON-NLS-1$
        "NESTING", // DB2 //$NON-NLS-1$
        "NEW", // DB2 //$NON-NLS-1$
        "NEW_TABLE", //$NON-NLS-1$
        "NEXT", // DB2 //$NON-NLS-1$
        "NO", //$NON-NLS-1$
        "NO_WRITE_TO_BINLOG", //$NON-NLS-1$
        "NOAUDIT", // DB2 //$NON-NLS-1$
        "NOCACHE", //$NON-NLS-1$
        "NOCHECK", //$NON-NLS-1$
        "NOCOMPRESS", //$NON-NLS-1$
        "NOCREATEDB", //$NON-NLS-1$
        "NOCREATEROLE", //$NON-NLS-1$
        "NOCREATEUSER", // DB2 //$NON-NLS-1$
        "NOCYCLE", // DB2 //$NON-NLS-1$
        "NODENAME", // DB2 //$NON-NLS-1$
        "NODENUMBER", //$NON-NLS-1$
        "NOINHERIT", //$NON-NLS-1$
        "NOLOGIN", // DB2 //$NON-NLS-1$
        "NOMAXVALUE", // DB2 //$NON-NLS-1$
        "NOMINVALUE", //$NON-NLS-1$
        "NONCLUSTERED", //$NON-NLS-1$
        "NONE", // DB2 //$NON-NLS-1$
        "NOORDER", //$NON-NLS-1$
        "NORMALIZE", //$NON-NLS-1$
        "NORMALIZED", //$NON-NLS-1$
        "NOSUPERUSER", // DB2 //$NON-NLS-1$
        "NOT", //$NON-NLS-1$
        "NOTHING", //$NON-NLS-1$
        "NOTIFY", //$NON-NLS-1$
        "NOTNULL", //$NON-NLS-1$
        "NOWAIT", // DB2 //$NON-NLS-1$
        "NULL", //$NON-NLS-1$
        "NULLABLE", //$NON-NLS-1$
        "NULLIF", // DB2 //$NON-NLS-1$
        "NULLS", //$NON-NLS-1$
        "NUMBER", //$NON-NLS-1$
        "NUMERIC", // DB2 //$NON-NLS-1$
        "NUMPARTS", // DB2 //$NON-NLS-1$
        "OBID", //$NON-NLS-1$
        "OBJECT", //$NON-NLS-1$
        "OCTET_LENGTH", //$NON-NLS-1$
        "OCTETS", // DB2 //$NON-NLS-1$
        "OF", //$NON-NLS-1$
        "OFF", //$NON-NLS-1$
        "OFFLINE", //$NON-NLS-1$
        "OFFSET", //$NON-NLS-1$
        "OFFSETS", //$NON-NLS-1$
        "OIDS", // DB2 //$NON-NLS-1$
        "OLD", // DB2 //$NON-NLS-1$
        "OLD_TABLE", // DB2 //$NON-NLS-1$
        "ON", //$NON-NLS-1$
        "ONLINE", //$NON-NLS-1$
        "ONLY", // DB2 //$NON-NLS-1$
        "OPEN", //$NON-NLS-1$
        "OPENDATASOURCE", //$NON-NLS-1$
        "OPENQUERY", //$NON-NLS-1$
        "OPENROWSET", //$NON-NLS-1$
        "OPENXML", //$NON-NLS-1$
        "OPERATION", //$NON-NLS-1$
        "OPERATOR", // DB2 //$NON-NLS-1$
        "OPTIMIZATION", // DB2 //$NON-NLS-1$
        "OPTIMIZE", // DB2 //$NON-NLS-1$
        "OPTION", //$NON-NLS-1$
        "OPTIONALLY", //$NON-NLS-1$
        "OPTIONS", // DB2 //$NON-NLS-1$
        "OR", // DB2 //$NON-NLS-1$
        "ORDER", //$NON-NLS-1$
        "ORDERING", //$NON-NLS-1$
        "ORDINALITY", //$NON-NLS-1$
        "OTHERS", // DB2 //$NON-NLS-1$
        "OUT", // DB2 //$NON-NLS-1$
        "OUTER", //$NON-NLS-1$
        "OUTFILE", //$NON-NLS-1$
        "OUTPUT", //$NON-NLS-1$
        "OVER", //$NON-NLS-1$
        "OVERLAPS", //$NON-NLS-1$
        "OVERLAY", // DB2 //$NON-NLS-1$
        "OVERRIDING", //$NON-NLS-1$
        "OWNER", //$NON-NLS-1$
        "PACK_KEYS", // DB2 //$NON-NLS-1$
        "PACKAGE", //$NON-NLS-1$
        "PAD", // DB2 //$NON-NLS-1$
        "PARAMETER", //$NON-NLS-1$
        "PARAMETER_MODE", //$NON-NLS-1$
        "PARAMETER_NAME", //$NON-NLS-1$
        "PARAMETER_ORDINAL_POSITION", //$NON-NLS-1$
        "PARAMETER_SPECIFIC_CATALOG", //$NON-NLS-1$
        "PARAMETER_SPECIFIC_NAME", //$NON-NLS-1$
        "PARAMETER_SPECIFIC_SCHEMA", //$NON-NLS-1$
        "PARAMETERS", // DB2 //$NON-NLS-1$
        "PART", //$NON-NLS-1$
        "PARTIAL", // DB2 //$NON-NLS-1$
        "PARTITION", //$NON-NLS-1$
        "PASCAL", //$NON-NLS-1$
        "PASSWORD", // DB2 //$NON-NLS-1$
        "PATH", //$NON-NLS-1$
        "PCTFREE", //$NON-NLS-1$
        "PERCENT", //$NON-NLS-1$
        "PERCENT_RANK", //$NON-NLS-1$
        "PERCENTILE_CONT", //$NON-NLS-1$
        "PERCENTILE_DISC", // DB2 //$NON-NLS-1$
        "PIECESIZE", //$NON-NLS-1$
        "PLACING", // DB2 //$NON-NLS-1$
        "PLAN", //$NON-NLS-1$
        "PLI", // DB2 //$NON-NLS-1$
        "POSITION", //$NON-NLS-1$
        "POSTFIX", //$NON-NLS-1$
        "POWER", //$NON-NLS-1$
        "PRECEDING", // DB2 //$NON-NLS-1$
        "PRECISION", //$NON-NLS-1$
        "PREFIX", //$NON-NLS-1$
        "PREORDER", // DB2 //$NON-NLS-1$
        "PREPARE", //$NON-NLS-1$
        "PREPARED", //$NON-NLS-1$
        "PRESERVE", // DB2 //$NON-NLS-1$
        "PRIMARY", //$NON-NLS-1$
        "PRINT", //$NON-NLS-1$
        "PRIOR", // DB2 //$NON-NLS-1$
        "PRIQTY", // DB2 //$NON-NLS-1$
        "PRIVILEGES", //$NON-NLS-1$
        "PROC", //$NON-NLS-1$
        "PROCEDURAL", // DB2 //$NON-NLS-1$
        "PROCEDURE", //$NON-NLS-1$
        "PROCESS", //$NON-NLS-1$
        "PROCESSLIST", // DB2 //$NON-NLS-1$
        "PROGRAM", // DB2 //$NON-NLS-1$
        "PSID", //$NON-NLS-1$
        "PUBLIC", //$NON-NLS-1$
        "PURGE", // DB2 //$NON-NLS-1$
        "QUERYNO", //$NON-NLS-1$
        "QUOTE", //$NON-NLS-1$
        "RAID0", //$NON-NLS-1$
        "RAISERROR", //$NON-NLS-1$
        "RANGE", //$NON-NLS-1$
        "RANK", //$NON-NLS-1$
        "RAW", // DB2 //$NON-NLS-1$
        "READ", // DB2 //$NON-NLS-1$
        "READS", //$NON-NLS-1$
        "READTEXT", //$NON-NLS-1$
        "REAL", //$NON-NLS-1$
        "RECHECK", //$NON-NLS-1$
        "RECONFIGURE", // DB2 //$NON-NLS-1$
        "RECOVERY", //$NON-NLS-1$
        "RECURSIVE", //$NON-NLS-1$
        "REF", // DB2 //$NON-NLS-1$
        "REFERENCES", // DB2 //$NON-NLS-1$
        "REFERENCING", //$NON-NLS-1$
        "REGEXP", //$NON-NLS-1$
        "REGR_AVGX", //$NON-NLS-1$
        "REGR_AVGY", //$NON-NLS-1$
        "REGR_COUNT", //$NON-NLS-1$
        "REGR_INTERCEPT", //$NON-NLS-1$
        "REGR_R2", //$NON-NLS-1$
        "REGR_SLOPE", //$NON-NLS-1$
        "REGR_SXX", //$NON-NLS-1$
        "REGR_SXY", //$NON-NLS-1$
        "REGR_SYY", //$NON-NLS-1$
        "REINDEX", //$NON-NLS-1$
        "RELATIVE", // DB2 //$NON-NLS-1$
        "RELEASE", //$NON-NLS-1$
        "RELOAD", // DB2 //$NON-NLS-1$
        "RENAME", // DB2 //$NON-NLS-1$
        "REPEAT", //$NON-NLS-1$
        "REPEATABLE", //$NON-NLS-1$
        "REPLACE", //$NON-NLS-1$
        "REPLICATION", //$NON-NLS-1$
        "REQUIRE", // DB2 //$NON-NLS-1$
        "RESET", // DB2 //$NON-NLS-1$
        "RESIGNAL", //$NON-NLS-1$
        "RESOURCE", // DB2 //$NON-NLS-1$
        "RESTART", //$NON-NLS-1$
        "RESTORE", // DB2 //$NON-NLS-1$
        "RESTRICT", // DB2 //$NON-NLS-1$
        "RESULT", // DB2 //$NON-NLS-1$
        "RESULT_SET_LOCATOR", // DB2 //$NON-NLS-1$
        "RETURN", //$NON-NLS-1$
        "RETURNED_CARDINALITY", //$NON-NLS-1$
        "RETURNED_LENGTH", //$NON-NLS-1$
        "RETURNED_OCTET_LENGTH", //$NON-NLS-1$
        "RETURNED_SQLSTATE", // DB2 //$NON-NLS-1$
        "RETURNS", // DB2 //$NON-NLS-1$
        "REVOKE", // DB2 //$NON-NLS-1$
        "RIGHT", //$NON-NLS-1$
        "RLIKE", //$NON-NLS-1$
        "ROLE", // DB2 //$NON-NLS-1$
        "ROLLBACK", //$NON-NLS-1$
        "ROLLUP", // DB2 //$NON-NLS-1$
        "ROUTINE", //$NON-NLS-1$
        "ROUTINE_CATALOG", //$NON-NLS-1$
        "ROUTINE_NAME", //$NON-NLS-1$
        "ROUTINE_SCHEMA", // DB2 //$NON-NLS-1$
        "ROW", //$NON-NLS-1$
        "ROW_COUNT", //$NON-NLS-1$
        "ROW_NUMBER", //$NON-NLS-1$
        "ROWCOUNT", //$NON-NLS-1$
        "ROWGUIDCOL", //$NON-NLS-1$
        "ROWID", //$NON-NLS-1$
        "ROWNUM", // DB2 //$NON-NLS-1$
        "ROWS", // DB2 //$NON-NLS-1$
        "RRN", //$NON-NLS-1$
        "RULE", // DB2 //$NON-NLS-1$
        "RUN", //$NON-NLS-1$
        "SAVE", // DB2 //$NON-NLS-1$
        "SAVEPOINT", //$NON-NLS-1$
        "SCALE", // DB2 //$NON-NLS-1$
        "SCHEMA", //$NON-NLS-1$
        "SCHEMA_NAME", //$NON-NLS-1$
        "SCHEMAS", //$NON-NLS-1$
        "SCOPE", //$NON-NLS-1$
        "SCOPE_CATALOG", //$NON-NLS-1$
        "SCOPE_NAME", //$NON-NLS-1$
        "SCOPE_SCHEMA", // DB2 //$NON-NLS-1$
        "SCRATCHPAD", //$NON-NLS-1$
        "SCROLL", //$NON-NLS-1$
        "SEARCH", // DB2 //$NON-NLS-1$
        "SECOND", //$NON-NLS-1$
        "SECOND_MICROSECOND", // DB2 //$NON-NLS-1$
        "SECONDS", // DB2 //$NON-NLS-1$
        "SECQTY", //$NON-NLS-1$
        "SECTION", // DB2 //$NON-NLS-1$
        "SECURITY", // DB2 //$NON-NLS-1$
        "SELECT", //$NON-NLS-1$
        "SELF", // DB2 //$NON-NLS-1$
        "SENSITIVE", //$NON-NLS-1$
        "SEPARATOR", //$NON-NLS-1$
        "SEQUENCE", //$NON-NLS-1$
        "SERIALIZABLE", //$NON-NLS-1$
        "SERVER_NAME", //$NON-NLS-1$
        "SESSION", //$NON-NLS-1$
        "SESSION_USER", // DB2 //$NON-NLS-1$
        "SET", //$NON-NLS-1$
        "SETOF", //$NON-NLS-1$
        "SETS", //$NON-NLS-1$
        "SETUSER", //$NON-NLS-1$
        "SHARE", //$NON-NLS-1$
        "SHOW", //$NON-NLS-1$
        "SHUTDOWN", // DB2 //$NON-NLS-1$
        "SIGNAL", //$NON-NLS-1$
        "SIMILAR", // DB2 //$NON-NLS-1$
        "SIMPLE", //$NON-NLS-1$
        "SIZE", //$NON-NLS-1$
        "SMALLINT", // DB2 //$NON-NLS-1$
        "SOME", //$NON-NLS-1$
        "SONAME", // DB2 //$NON-NLS-1$
        "SOURCE", //$NON-NLS-1$
        "SPACE", //$NON-NLS-1$
        "SPATIAL", // DB2 //$NON-NLS-1$
        "SPECIFIC", //$NON-NLS-1$
        "SPECIFIC_NAME", //$NON-NLS-1$
        "SPECIFICTYPE", // DB2 //$NON-NLS-1$
        "SQL", //$NON-NLS-1$
        "SQL_BIG_RESULT", //$NON-NLS-1$
        "SQL_BIG_SELECTS", //$NON-NLS-1$
        "SQL_BIG_TABLES", //$NON-NLS-1$
        "SQL_CALC_FOUND_ROWS", //$NON-NLS-1$
        "SQL_LOG_OFF", //$NON-NLS-1$
        "SQL_LOG_UPDATE", //$NON-NLS-1$
        "SQL_LOW_PRIORITY_UPDATES", //$NON-NLS-1$
        "SQL_SELECT_LIMIT", //$NON-NLS-1$
        "SQL_SMALL_RESULT", //$NON-NLS-1$
        "SQL_WARNINGS", //$NON-NLS-1$
        "SQLCA", //$NON-NLS-1$
        "SQLCODE", //$NON-NLS-1$
        "SQLERROR", //$NON-NLS-1$
        "SQLEXCEPTION", // DB2 //$NON-NLS-1$
        "SQLID", //$NON-NLS-1$
        "SQLSTATE", //$NON-NLS-1$
        "SQLWARNING", //$NON-NLS-1$
        "SQRT", //$NON-NLS-1$
        "SSL", //$NON-NLS-1$
        "STABLE", // DB2 //$NON-NLS-1$
        "STANDARD", // DB2 //$NON-NLS-1$
        "START", //$NON-NLS-1$
        "STARTING", //$NON-NLS-1$
        "STATE", //$NON-NLS-1$
        "STATEMENT", // DB2 //$NON-NLS-1$
        "STATIC", //$NON-NLS-1$
        "STATISTICS", //$NON-NLS-1$
        "STATUS", // DB2 //$NON-NLS-1$
        "STAY", //$NON-NLS-1$
        "STDDEV_POP", //$NON-NLS-1$
        "STDDEV_SAMP", //$NON-NLS-1$
        "STDIN", //$NON-NLS-1$
        "STDOUT", // DB2 //$NON-NLS-1$
        "STOGROUP", //$NON-NLS-1$
        "STORAGE", // DB2 //$NON-NLS-1$
        "STORES", //$NON-NLS-1$
        "STRAIGHT_JOIN", //$NON-NLS-1$
        "STRICT", //$NON-NLS-1$
        "STRING", //$NON-NLS-1$
        "STRUCTURE", // DB2 //$NON-NLS-1$
        "STYLE", //$NON-NLS-1$
        "SUBCLASS_ORIGIN", //$NON-NLS-1$
        "SUBLIST", //$NON-NLS-1$
        "SUBMULTISET", // DB2 //$NON-NLS-1$
        "SUBPAGES", // DB2 //$NON-NLS-1$
        "SUBSTRING", //$NON-NLS-1$
        "SUCCESSFUL", //$NON-NLS-1$
        "SUM", //$NON-NLS-1$
        "SUPERUSER", //$NON-NLS-1$
        "SYMMETRIC", // DB2 //$NON-NLS-1$
        "SYNONYM", //$NON-NLS-1$
        "SYSDATE", // DB2 //$NON-NLS-1$
        "SYSFUN", // DB2 //$NON-NLS-1$
        "SYSIBM", //$NON-NLS-1$
        "SYSID", // DB2 //$NON-NLS-1$
        "SYSPROC", // DB2 //$NON-NLS-1$
        "SYSTEM", //$NON-NLS-1$
        "SYSTEM_USER", // DB2 //$NON-NLS-1$
        "TABLE", //$NON-NLS-1$
        "TABLE_NAME", //$NON-NLS-1$
        "TABLES", //$NON-NLS-1$
        "TABLESAMPLE", // DB2 //$NON-NLS-1$
        "TABLESPACE", //$NON-NLS-1$
        "TEMP", //$NON-NLS-1$
        "TEMPLATE", //$NON-NLS-1$
        "TEMPORARY", //$NON-NLS-1$
        "TERMINATE", //$NON-NLS-1$
        "TERMINATED", //$NON-NLS-1$
        "TEXT", //$NON-NLS-1$
        "TEXTSIZE", //$NON-NLS-1$
        "THAN", // DB2 //$NON-NLS-1$
        "THEN", //$NON-NLS-1$
        "TIES", //$NON-NLS-1$
        "TIME", //$NON-NLS-1$
        "TIMESTAMP", //$NON-NLS-1$
        "TIMEZONE_HOUR", //$NON-NLS-1$
        "TIMEZONE_MINUTE", //$NON-NLS-1$
        "TINYBLOB", //$NON-NLS-1$
        "TINYINT", //$NON-NLS-1$
        "TINYTEXT", // DB2 //$NON-NLS-1$
        "TO", //$NON-NLS-1$
        "TOAST", //$NON-NLS-1$
        "TOP", //$NON-NLS-1$
        "TOP_LEVEL_COUNT", //$NON-NLS-1$
        "TRAILING", //$NON-NLS-1$
        "TRAN", // DB2 //$NON-NLS-1$
        "TRANSACTION", //$NON-NLS-1$
        "TRANSACTION_ACTIVE", //$NON-NLS-1$
        "TRANSACTIONS_COMMITTED", //$NON-NLS-1$
        "TRANSACTIONS_ROLLED_BACK", //$NON-NLS-1$
        "TRANSFORM", //$NON-NLS-1$
        "TRANSFORMS", //$NON-NLS-1$
        "TRANSLATE", //$NON-NLS-1$
        "TRANSLATION", //$NON-NLS-1$
        "TREAT", // DB2 //$NON-NLS-1$
        "TRIGGER", //$NON-NLS-1$
        "TRIGGER_CATALOG", //$NON-NLS-1$
        "TRIGGER_NAME", //$NON-NLS-1$
        "TRIGGER_SCHEMA", // DB2 //$NON-NLS-1$
        "TRIM", //$NON-NLS-1$
        "TRUE", //$NON-NLS-1$
        "TRUNCATE", //$NON-NLS-1$
        "TRUSTED", //$NON-NLS-1$
        "TSEQUAL", // DB2 //$NON-NLS-1$
        "TYPE", //$NON-NLS-1$
        "UESCAPE", //$NON-NLS-1$
        "UID", //$NON-NLS-1$
        "UNBOUNDED", //$NON-NLS-1$
        "UNCOMMITTED", //$NON-NLS-1$
        "UNDER", // DB2 //$NON-NLS-1$
        "UNDO", //$NON-NLS-1$
        "UNENCRYPTED", // DB2 //$NON-NLS-1$
        "UNION", // DB2 //$NON-NLS-1$
        "UNIQUE", //$NON-NLS-1$
        "UNKNOWN", //$NON-NLS-1$
        "UNLISTEN", //$NON-NLS-1$
        "UNLOCK", //$NON-NLS-1$
        "UNNAMED", //$NON-NLS-1$
        "UNNEST", //$NON-NLS-1$
        "UNSIGNED", // DB2 //$NON-NLS-1$
        "UNTIL", // DB2 //$NON-NLS-1$
        "UPDATE", //$NON-NLS-1$
        "UPDATETEXT", //$NON-NLS-1$
        "UPPER", // DB2 //$NON-NLS-1$
        "USAGE", //$NON-NLS-1$
        "USE", // DB2 //$NON-NLS-1$
        "USER", //$NON-NLS-1$
        "USER_DEFINED_TYPE_CATALOG", //$NON-NLS-1$
        "USER_DEFINED_TYPE_CODE", //$NON-NLS-1$
        "USER_DEFINED_TYPE_NAME", //$NON-NLS-1$
        "USER_DEFINED_TYPE_SCHEMA", // DB2 //$NON-NLS-1$
        "USING", //$NON-NLS-1$
        "UTC_DATE", //$NON-NLS-1$
        "UTC_TIME", //$NON-NLS-1$
        "UTC_TIMESTAMP", //$NON-NLS-1$
        "VACUUM", //$NON-NLS-1$
        "VALID", //$NON-NLS-1$
        "VALIDATE", //$NON-NLS-1$
        "VALIDATOR", // DB2 //$NON-NLS-1$
        "VALIDPROC", //$NON-NLS-1$
        "VALUE", // DB2 //$NON-NLS-1$
        "VALUES", //$NON-NLS-1$
        "VAR_POP", //$NON-NLS-1$
        "VAR_SAMP", //$NON-NLS-1$
        "VARBINARY", //$NON-NLS-1$
        "VARCHAR", //$NON-NLS-1$
        "VARCHAR2", //$NON-NLS-1$
        "VARCHARACTER", // DB2 //$NON-NLS-1$
        "VARIABLE", //$NON-NLS-1$
        "VARIABLES", // DB2 //$NON-NLS-1$
        "VARIANT", //$NON-NLS-1$
        "VARYING", // DB2 //$NON-NLS-1$
        "VCAT", //$NON-NLS-1$
        "VERBOSE", // DB2 //$NON-NLS-1$
        "VIEW", //MySQL #272
        "VIRTUAL", //$NON-NLS-1$
        "VOLATILE", // DB2 //$NON-NLS-1$
        "VOLUMES", //$NON-NLS-1$
        "WAITFOR", // DB2 //$NON-NLS-1$
        "WHEN", //$NON-NLS-1$
        "WHENEVER", // DB2 //$NON-NLS-1$
        "WHERE", // DB2 //$NON-NLS-1$
        "WHILE", //$NON-NLS-1$
        "WIDTH_BUCKET", //$NON-NLS-1$
        "WINDOW", // DB2 //$NON-NLS-1$
        "WITH", //$NON-NLS-1$
        "WITHIN", //$NON-NLS-1$
        "WITHOUT", // DB2 //$NON-NLS-1$
        "WLM", //$NON-NLS-1$
        "WORK", // DB2 //$NON-NLS-1$
        "WRITE", //$NON-NLS-1$
        "WRITETEXT", //$NON-NLS-1$
        "X509", //$NON-NLS-1$
        "XOR", // DB2 //$NON-NLS-1$
        "YEAR", //$NON-NLS-1$
        "YEAR_MONTH", // DB2 //$NON-NLS-1$
        "YEARS", //$NON-NLS-1$
        "ZEROFILL", //$NON-NLS-1$
        "ZONE" };
        RESERVED_WORDS = new HashSet<String>(words.length);
        for (String word : words) {
            RESERVED_WORDS.add(word);
        }
    }

    /**
     * Utility class - no instances allowed.
     */
    private SqlReservedWords() {
    }

    public static boolean containsWord(String word) {
        boolean rc;
        if (word == null) {
            rc = false;
        } else {
            rc = RESERVED_WORDS.contains(word.toUpperCase());
        }
        return rc;
    }
}
