package ru.perm.v.hr_admin.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты валидации responsive-поведения навигации.
 * Проверяют, что sidebar виден только на десктопе,
 * а bottom-nav — только на мобильных устройствах.
 */
class NavigationResponsiveTest {

    private static final String TEMPLATES_DIR = "src/main/resources/templates/";

    @Test
    @DisplayName("Sidebar должен быть скрыт на мобильных и виден на десктопе (hidden md:flex)")
    void sidebarShouldBeHiddenOnMobileAndVisibleOnDesktop() throws IOException {
        String sidebarContent = readFile("fragments/sidebar.ftl");

        // Проверяем, что sidebar имеет класс 'hidden' (скрыт по умолчанию)
        assertTrue(sidebarContent.contains("hidden"),
                "Sidebar должен иметь класс 'hidden' для скрытия на мобильных устройствах");

        // Проверяем, что sidebar имеет класс 'md:flex' (виден на md и больше)
        assertTrue(sidebarContent.contains("md:flex"),
                "Sidebar должен иметь класс 'md:flex' для отображения на десктопе");

        // Убеждаемся, что sidebar НЕ имеет 'fixed' (не прижат к краю)
        assertFalse(sidebarContent.contains("fixed"),
                "Sidebar не должен иметь класс 'fixed', так как это боковая панель");
    }

    @Test
    @DisplayName("Sidebar не должен иметь Alpine.js toggle-логику")
    void sidebarShouldNotHaveAlpineToggleLogic() throws IOException {
        String sidebarContent = readFile("fragments/sidebar.ftl");

        assertFalse(sidebarContent.contains("Alpine.data"),
                "Sidebar не должен содержать Alpine.js определения данных");
        assertFalse(sidebarContent.contains("@click=\"toggle()\""),
                "Sidebar не должен содержать кнопку toggle");
        assertFalse(sidebarContent.contains("localStorage"),
                "Sidebar не должен использовать localStorage для сохранения состояния");
    }

    @Test
    @DisplayName("Bottom navigation должен быть скрыт на десктопе и виден на мобильных (md:hidden)")
    void bottomNavigationShouldBeHiddenOnDesktopAndVisibleOnMobile() throws IOException {
        String bottomNavContent = readFile("fragments/bottom-navigation.ftl");

        // Проверяем, что bottom-nav скрыт на md и больше
        assertTrue(bottomNavContent.contains("md:hidden"),
                "Bottom navigation должен иметь класс 'md:hidden' для скрытия на десктопе");

        // Проверяем, что bottom-nav прижат к низу экрана
        assertTrue(bottomNavContent.contains("fixed bottom-0"),
                "Bottom navigation должен быть прикреплён к низу экрана через 'fixed bottom-0'");

        // Проверяем, что bottom-nav имеет высокий z-index (поверх контента)
        assertTrue(bottomNavContent.contains("z-50") || bottomNavContent.contains("z-[50]"),
                "Bottom navigation должен иметь z-index для отображения поверх контента");
    }

    @Test
    @DisplayName("Bottom navigation должен содержать все пункты навигации")
    void bottomNavigationShouldHaveAllNavigationItems() throws IOException {
        String bottomNavContent = readFile("fragments/bottom-navigation.ftl");

        String[] expectedLinks = {
                "/employees/",
                "/employees/new",
                "/employees/filter_employees",
                "/employees/show_employees",
                "/positions/"
        };

        for (String link : expectedLinks) {
            assertTrue(bottomNavContent.contains(link),
                    "Bottom navigation должен содержать ссылку: " + link);
        }
    }

    @Test
    @DisplayName("Bottom navigation должен иметь tooltip при hover")
    void bottomNavigationShouldHaveTooltipsOnHover() throws IOException {
        String bottomNavContent = readFile("fragments/bottom-navigation.ftl");

        // Проверяем использование group-hover для tooltip
        assertTrue(bottomNavContent.contains("group-hover:opacity-100"),
                "Tooltip должен появляться при hover через 'group-hover:opacity-100'");
        assertTrue(bottomNavContent.contains("opacity-0"),
                "Tooltip должен быть скрыт по умолчанию через 'opacity-0'");

        // Проверяем, что все пункты имеют подписи
        String[] expectedLabels = {"Сотрудники", "Добавить", "Найти", "Список", "Должности"};
        for (String label : expectedLabels) {
            assertTrue(bottomNavContent.contains(label),
                    "Bottom navigation должен иметь подпись: " + label);
        }
    }

    @Test
    @DisplayName("Index page должен иметь padding-bottom для bottom navigation")
    void indexPageShouldHaveBottomPaddingForBottomNav() throws IOException {
        String indexContent = readFile("index.ftlh");

        // Проверяем наличие padding-bottom на main элементе
        assertTrue(indexContent.contains("pb-20") || indexContent.contains("pb-16") || indexContent.contains("pb-12"),
                "Основной контент должен иметь нижний отступ (pb-16 или pb-20) для отображения под bottom navigation");
    }

    @Test
    @DisplayName("Index page должен включать bottom navigation")
    void indexPageShouldIncludeBottomNavigation() throws IOException {
        String indexContent = readFile("index.ftlh");

        assertTrue(indexContent.contains("bottom-navigation.ftl") ||
                        indexContent.contains("bottom-navigation"),
                "Index page должен включать fragment bottom-navigation.ftl");
    }

    @Test
    @DisplayName("Sidebar не должен содержать кнопки 'Скрыть панель'")
    void sidebarShouldNotHaveHideButton() throws IOException {
        String sidebarContent = readFile("fragments/sidebar.ftl");

        assertFalse(sidebarContent.contains("Скрыть панель"),
                "Sidebar не должен содержать кнопку 'Скрыть панель'");
        assertFalse(sidebarContent.contains("toggle"),
                "Sidebar не должен содержать логику toggle");
    }

    private String readFile(String relativePath) throws IOException {
        String filePath = TEMPLATES_DIR + relativePath;
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
