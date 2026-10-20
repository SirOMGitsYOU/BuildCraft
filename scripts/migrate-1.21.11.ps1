# Bulk mechanical migrations for BuildCraft 1.21.11 port
$root = "C:\Users\Peter\Documents\GitHub\BuildCraft"
$dirs = @(
    "$root\common",
    "$root\BuildCraftAPI\api",
    "$root\sub_projects\expression\src"
)

$javaFiles = $dirs | ForEach-Object { if (Test-Path $_) { Get-ChildItem -Path $_ -Recurse -Filter *.java } } | Where-Object { $_.FullName -notmatch '\\datagen\\' }

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw
    $original = $content

    # @Nonnull -> @NotNull
    if ($content -match '@Nonnull') {
        $content = $content -replace '@Nonnull', '@NotNull'
        if ($content -notmatch 'import org\.jetbrains\.annotations\.NotNull') {
            if ($content -match '(?m)^package .+\r?\n') {
                $content = $content -replace '(?m)^package .+\r?\n', "`$&import org.jetbrains.annotations.NotNull;`n"
            }
        }
    }

    # LazyLoadedValue package
    $content = $content -replace 'import net\.minecraft\.util\.LazyLoadedValue;', 'import buildcraft.lib.misc.LazyLoadedValue;'

    # Profiler access
    $content = $content -replace 'Minecraft\.getInstance\(\)\.getProfiler\(\)', 'net.minecraft.util.profiling.Profiler.get()'
    if ($content -match 'Profiler\.get\(\)' -and $content -notmatch 'import net\.minecraft\.util\.profiling\.Profiler;') {
        if ($content -match '(?m)^package .+\r?\n') {
            $content = $content -replace '(?m)^package .+\r?\n', "`$&import net.minecraft.util.profiling.Profiler;`n"
        }
    }

    # ModelResourceLocation import removal
    $content = $content -replace 'import net\.minecraft\.client\.resources\.model\.ModelResourceLocation;\r?\n', ''

    # InteractionResultHolder -> InteractionResult (imports only; method bodies need manual follow-up)
    $content = $content -replace 'import net\.minecraft\.world\.InteractionResultHolder;\r?\n', ''
    $content = $content -replace 'InteractionResultHolder<([^>]+)>', 'InteractionResult'
    if ($content -match 'InteractionResult' -and $content -notmatch 'import net\.minecraft\.world\.InteractionResult;') {
        if ($content -match '(?m)^package .+\r?\n') {
            $content = $content -replace '(?m)^package .+\r?\n', "`$&import net.minecraft.world.InteractionResult;`n"
        }
    }

    # ObjectHolder removal
    $content = $content -replace 'import net\.minecraftforge\.registries\.ObjectHolder;\r?\n', ''
    $content = $content -replace '@ObjectHolder\([^\)]*\)\r?\n\s*', ''

    # FakePlayer import removal (FakePlayerBC extends ServerPlayer)
    $content = $content -replace 'import net\.minecraftforge\.common\.util\.FakePlayer;\r?\n', ''

    # BakedModel import from resources.model (removed)
    $content = $content -replace 'import net\.minecraft\.client\.resources\.model\.BakedModel;\r?\n', ''

    # Identifier constructor fixes
    $content = $content -replace 'new Identifier\(([^,]+),\s*([^)]+)\)', 'Identifier.fromNamespaceAndPath($1, $2)'
    $content = $content -replace 'new Identifier\(([^)]+)\)', 'Identifier.parse($1)'

    # Util import fix for RegistrationHelper
    $content = $content -replace 'import net\.minecraft\.Util;\r?\n', 'import net.minecraft.util.Util;'

    # CompoundTag common patterns
    $content = $content -replace '(\w+)\.getString\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\))', '$1.getStringOr($2, "")'
    $content = $content -replace '(\w+)\.getInt\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getIntOr($2, 0)'
    $content = $content -replace '(\w+)\.getBoolean\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getBooleanOr($2, false)'
    $content = $content -replace '(\w+)\.getLong\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getLongOr($2, 0L)'
    $content = $content -replace '(\w+)\.getDouble\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getDoubleOr($2, 0.0)'
    $content = $content -replace '(\w+)\.getFloat\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getFloatOr($2, 0.0f)'
    $content = $content -replace '(\w+)\.getList\(([^)]+)\)(?!\s*\.orElse|\s*\.get\(\)|Or)', '$1.getListOrEmpty($2)'

    # Direction.VALUES -> Direction.values()
    $content = $content -replace 'Direction\.VALUES', 'Direction.values()'

    if ($content -ne $original) {
        Set-Content -Path $file.FullName -Value $content -NoNewline
        Write-Host "Updated: $($file.FullName)"
    }
}

Write-Host "Migration complete. Files touched: $($javaFiles.Count)"
