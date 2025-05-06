def call(apps, action) {
    buildStageList = []
    buildParallelMap = [:]
    apps.each { app, value ->
        if (action == "build") {
            buildParallelMap.put(app, stageBuildCreate(app, value))
        }
        if (action == "upload") {
            buildParallelMap.put(app, stageUploadCreate(app, value))
        }
    }
    buildStageList.add(buildParallelMap)
    return buildStageList
}