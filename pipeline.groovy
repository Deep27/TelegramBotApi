
checkout([
    $class: 'GitSCM',
    branches: [[name: '*/master']],
    doGenerateSubmoduleConfigurations: false,
    extensions: [],
    submoduleCfg: [],
    userRemoteConfigs: [[
        credentialsId: '44d9be2b-0610-4a74-9f0f-ed6254035960',
        url: 'https://github.com/Deep27/DeepAnonymizerBot.git'
    ]]
])
