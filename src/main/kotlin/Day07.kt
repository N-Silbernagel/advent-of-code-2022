fun main() {
    val input = readFileAsList("Day07")
    println(Day07.part1(input))
    println(Day07.part2(input))
}

object Day07 {
    fun part1(input: List<String>): Int {
        val rootDir = buildFileSystem(input)

        val dirSizeMap = mutableMapOf<Dir, Int>()
        calculateSize(rootDir, dirSizeMap)

        var totalSize = 0
        for (size in dirSizeMap.values) {
            if (size <= 100000) {
                totalSize += size
            }
        }

        return totalSize
    }

    fun part2(input: List<String>): Int {
        val rootDir = buildFileSystem(input)

        val dirSizeMap = mutableMapOf<Dir, Int>()
        calculateSize(rootDir, dirSizeMap)

        val totalSpace = 70000000
        val neededSpace = 30000000
        val rootDirSize = dirSizeMap.getOrDefault(rootDir, totalSpace)
        val freeSpace = totalSpace - rootDirSize
        val spaceLeft = neededSpace - freeSpace

        var bestDirSize = rootDirSize
        for (size in dirSizeMap.values) {
            if (size in spaceLeft until bestDirSize) {
                bestDirSize = size
            }
        }

        return bestDirSize
    }

    private fun calculateSize(dir: Dir, dirSizeMap: MutableMap<Dir, Int>): Int {
        var size = 0

        for (content in dir.content) {
            if (content is File) {
                size += content.fileSize
            } else if (content is Dir) {
                size += calculateSize(content, dirSizeMap)
            }
        }

        dirSizeMap[dir] = size
        return size
    }

    private fun buildFileSystem(input: List<String>): Dir {
        val commands = parseCommands(input)

        val rootDir = Dir("/", null)

        applyCommands(commands, rootDir)
        return rootDir
    }

    private fun applyCommands(
        commands: MutableList<Command>,
        rootDir: Dir,
    ) {
        val dirs = HashMap<String, Dir>()
        var currentDir = rootDir
        for (command in commands) {
            if (command.name == "cd") {
                currentDir = applyCdCommand(command, currentDir, rootDir, dirs)
            } else if (command.name == "ls") {
                applyLsCommand(command, currentDir, dirs)
            }
        }
    }

    private fun applyLsCommand(
        command: Command,
        currentDir: Dir,
        dirs: HashMap<String, Dir>
    ) {
        val fileRegex = "(\\d+) (.+)".toRegex()
        val dirRegex = "dir (.+)".toRegex()
        for (output in command.output) {
            val fileRegexMatches = fileRegex.find(output)
            val dirRegexMatches = dirRegex.find(output)

            if (fileRegexMatches != null) {
                addFileToDir(fileRegexMatches, currentDir)
            } else if (dirRegexMatches != null) {
                addDirToDir(dirRegexMatches, currentDir, dirs)
            }
        }
    }

    private fun addDirToDir(
        dirRegexMatches: MatchResult,
        currentDir: Dir,
        dirs: HashMap<String, Dir>
    ) {
        val dirName = dirRegexMatches.groupValues[1]

        val path = "${currentDir.path}/${dirName}"
            .replace("//", "/")
        val dir = dirs.getOrPut(path) {
            Dir(path, currentDir)
        }
        currentDir.content.add(dir)
    }

    private fun addFileToDir(fileRegexMatches: MatchResult, currentDir: Dir) {
        val fileSize = fileRegexMatches.groupValues[1].toInt()
        val fileName = fileRegexMatches.groupValues[2]

        val file = File(fileName, currentDir, fileSize)

        currentDir.content
            .add(file)
    }

    private fun applyCdCommand(
        command: Command,
        currentDir: Dir,
        rootDir: Dir,
        dirs: HashMap<String, Dir>
    ): Dir {
        if (command.argument == null) {
            throw RuntimeException()
        }

        if (command.argument == "..") {
            return currentDir.parent ?: rootDir
        }

        if (command.argument == "/") {
            return rootDir
        }

        val path = "${currentDir.path}/${command.argument}"
            .replace("//", "/")

        val newDir = Dir(
            "${currentDir.path}/${command.argument}",
            currentDir
        )

        return dirs.getOrPut(path) { newDir }
    }

    private fun parseCommands(input: List<String>): MutableList<Command> {
        val commands = mutableListOf<Command>()

        val commandRegex = "\\\$ (\\w+)(?: (.+))?".toRegex()
        for (line in input) {
            val regexResult = commandRegex.find(line)

            if (regexResult != null) {
                val command = regexResult.groupValues[1]
                val argument = regexResult.groupValues[2]

                commands.add(Command(command, argument))
            } else {
                commands.last()
                    .output
                    .add(line)
            }
        }

        return commands
    }

    interface FileSystemEntity

    data class File(val name: String, val dir: Dir, val fileSize: Int) : FileSystemEntity

    data class Dir(
        val path: String,
        val parent: Dir?,
        val content: MutableList<FileSystemEntity> = mutableListOf()
    ) : FileSystemEntity {
        override fun hashCode(): Int {
            return path.hashCode()
        }

        override fun toString(): String {
            return path
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Dir

            if (path != other.path) return false

            return true
        }
    }

    data class Command(val name: String, val argument: String?, val output: MutableList<String> = mutableListOf())
}
