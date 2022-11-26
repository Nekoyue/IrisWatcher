fun getResource(path: String) = object {}.javaClass.classLoader.getResource(path)!!.readText()
