package mainContext.dataclass

enum class BattleGroupStep {
    GetPowerHostileCreep,
    WaitExploreRoom,
    WaitBuildGroup,
    GotoNeedRoom,
    Battle,
    Sleep
}