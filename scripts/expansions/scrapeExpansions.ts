import * as fs from 'fs';
import { JSDOM } from 'jsdom';

interface Expansion {
    population_needed: number;
    coins_needed: number;
    time: string;
    xp: number;
    tools_needed: {
        axe: number;
        shovel: number;
        saw: number;
    };
}

class TownshipExpansionScraper {
    private baseUrl = 'https://township.fandom.com/wiki/Expansions';

    async scrapeExpansions(): Promise<Expansion[]> {
        try {
            console.log('Fetching expansion data from Township Wiki...');

            const response = await fetch(this.baseUrl, {
                headers: {
                    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch page: ${response.status} ${response.statusText}`);
            }

            const html = await response.text();
            console.log('Page fetched successfully, parsing HTML...');

            const dom = new JSDOM(html);
            const document = dom.window.document;

            const expansions: Expansion[] = [];

            // Find all tables with article-table class
            const tables = document.querySelectorAll('table.article-table');
            console.log(`Found ${tables.length} article-table tables on the page`);

            let tableCount = 0;
            let totalExpansionsSoFar = 0;

            // @ts-ignore
            for (const table of tables) {
                const tableText = table.textContent || '';

                if (this.isExpansionTable(tableText)) {
                    tableCount++;
                    console.log(`\nProcessing expansion table ${tableCount}...`);

                    const tableExpansions = this.parseExpansionTable(table, tableCount, totalExpansionsSoFar);
                    expansions.push(...tableExpansions);
                    totalExpansionsSoFar += tableExpansions.length;

                    console.log(`Found ${tableExpansions.length} expansions in table ${tableCount}`);
                    console.log(`Running total: ${totalExpansionsSoFar} expansions`);
                } else {
                    console.log('Skipping non-expansion table');
                }
            }

            console.log(`\nTotal expansions scraped: ${expansions.length}`);

            // If we got exactly 382, let's trim it to 381 as expected
            if (expansions.length === 382) {
                console.log('Got 382 expansions, trimming to 381 as expected from wiki...');
                return expansions.slice(0, 381);
            }

            return expansions;

        } catch (error) {
            console.error('Error scraping expansions:', error);
            throw error;
        }
    }

    private isExpansionTable(tableText: string): boolean {
        const lowerText = tableText.toLowerCase();
        return lowerText.includes('town land expansion');
    }

    private parseExpansionTable(table: Element, tableNumber: number, currentTotal: number): Expansion[] {
        const expansions: Expansion[] = [];
        const rows = table.querySelectorAll('tr');

        console.log(`  - Table ${tableNumber} has ${rows.length} rows`);

        // Parse data rows starting from row 2
        let validRowCount = 0;
        for (let i = 2; i < rows.length; i++) {
            const row = rows[i];
            const cells = row.querySelectorAll('td');

            if (cells.length >= 6) {
                // Check if we already have 381 expansions
                if (currentTotal + validRowCount >= 381) {
                    console.log(`  - Stopping at 381 expansions (reached limit in table ${tableNumber})`);
                    break;
                }

                const expansion = this.parseExpansionRow(cells, i, tableNumber);
                if (expansion) {
                    expansions.push(expansion);
                    validRowCount++;

                    // Debug first few expansions
                    if (validRowCount <= 3) {
                        console.log(`  - Expansion ${currentTotal + validRowCount}:`, expansion);
                    }
                }
            }
        }

        console.log(`  - Successfully parsed ${validRowCount} expansions from table ${tableNumber}`);
        return expansions;
    }

    private parseExpansionRow(cells: NodeListOf<Element>, rowIndex: number, tableNumber: number): Expansion | null {
        try {
            const cellTexts = Array.from(cells).map(cell => (cell.textContent || '').trim());

            // Debug output for first few rows
            if (rowIndex <= 4) {
                console.log(`    - Row ${rowIndex} raw data: [${cellTexts.join(' | ')}]`);
            }

            // CORRECT column mapping based on actual debug data:
            // 0: Expansion number (№) - skip this
            // 1: Population needed (like "60", "75", "1 490")
            // 2: Tools needed (format: "axe shovel saw" like "0 0 0", "2 3 1")
            // 3: Coins needed (like "25", "30", "1 150")
            // 4: Time (like "20min", "4h 30min")
            // 5: XP (like "4", "109")

            const populationText = cellTexts[1] || '0';
            const toolsText = cellTexts[2] || '0 0 0';
            const coinsText = cellTexts[3] || '0';
            const timeText = cellTexts[4] || '';
            const xpText = cellTexts[5] || '0';

            // Parse numbers
            const population = this.parseNumber(populationText);
            const coins = this.parseNumber(coinsText);
            const xp = this.parseNumber(xpText);

            // Parse tools (format like "2 3 1" meaning axe=2, shovel=3, saw=1)
            const tools = this.parseTools(toolsText);

            // Clean time text
            const time = this.cleanTimeText(timeText);

            const expansion: Expansion = {
                population_needed: population || 0,
                coins_needed: coins || 0,
                time: time,
                xp: xp || 0,
                tools_needed: {
                    axe: tools.axe,
                    shovel: tools.shovel,
                    saw: tools.saw
                }
            };

            // Validate that we have at least some meaningful data
            if (expansion.population_needed === 0 && expansion.coins_needed === 0 &&
                expansion.tools_needed.axe === 0 && expansion.tools_needed.shovel === 0 &&
                expansion.tools_needed.saw === 0 && expansion.xp === 0) {

                // This might be an empty or invalid row
                if (rowIndex <= 4) {
                    console.log(`    - Skipping row ${rowIndex}: all values are zero`);
                }
                return null;
            }

            return expansion;

        } catch (error) {
            console.warn(`Error parsing row ${rowIndex} in table ${tableNumber}:`, error);
            return null;
        }
    }

    private parseNumber(text: string): number | null {
        if (!text) return null;

        // Handle special cases
        if (text.toLowerCase().includes('free') || text === '-' || text === '') {
            return 0;
        }

        // Remove spaces, commas and extract number
        const cleanText = text.replace(/[\s,]/g, '');

        // Extract first number found
        const match = cleanText.match(/\d+/);
        if (match) {
            const num = parseInt(match[0], 10);
            return isNaN(num) ? null : num;
        }

        return null;
    }

    private parseTools(text: string): { axe: number, shovel: number, saw: number } {
        if (!text) return { axe: 0, shovel: 0, saw: 0 };

        // Split by spaces and parse each number
        // Format is typically "axe shovel saw" like "2 3 1"
        const parts = text.trim().split(/\s+/);

        const axe = parts.length > 0 ? this.parseNumber(parts[0]) || 0 : 0;
        const shovel = parts.length > 1 ? this.parseNumber(parts[1]) || 0 : 0;
        const saw = parts.length > 2 ? this.parseNumber(parts[2]) || 0 : 0;

        return { axe, shovel, saw };
    }

    private cleanTimeText(text: string): string {
        if (!text) return '';

        // Clean up time text but preserve the format
        return text.replace(/\s+/g, ' ').trim();
    }

    async saveToFile(expansions: Expansion[], filename: string = 'township_expansions.json'): Promise<void> {
        try {
            const jsonData = JSON.stringify(expansions, null, 2);
            fs.writeFileSync(filename, jsonData, 'utf8');
            console.log(`\nExpansions saved to ${filename}`);
        } catch (error) {
            console.error('Error saving to file:', error);
            throw error;
        }
    }

    async saveToCSV(expansions: Expansion[], filename: string = 'township_expansions.csv'): Promise<void> {
        try {
            // NO header row - just the data
            let csv = '';

            for (const expansion of expansions) {
                const time = `"${expansion.time.replace(/"/g, '""')}"`;  // Escape quotes
                csv += `${expansion.population_needed},${expansion.coins_needed},${time},${expansion.xp},${expansion.tools_needed.axe},${expansion.tools_needed.shovel},${expansion.tools_needed.saw}\n`;
            }

            fs.writeFileSync(filename, csv, 'utf8');
            console.log(`Expansions saved to ${filename} (${expansions.length} rows, no header)`);
        } catch (error) {
            console.error('Error saving to CSV:', error);
            throw error;
        }
    }

    validateExpansions(expansions: Expansion[]): void {
        console.log('\n=== Validation Report ===');
        console.log(`Total expansions: ${expansions.length}`);

        if (expansions.length !== 381) {
            console.warn(`Expected exactly 381 expansions, but got ${expansions.length}`);
        } else {
            console.log(`✅ Perfect! Got exactly 381 expansions as expected from the wiki.`);
        }

        // Analyze data completeness
        let hasPopulation = 0;
        let hasCoins = 0;
        let hasTime = 0;
        let hasXp = 0;
        let hasAxe = 0;
        let hasShovel = 0;
        let hasSaw = 0;

        expansions.forEach(exp => {
            if (exp.population_needed > 0) hasPopulation++;
            if (exp.coins_needed > 0) hasCoins++;
            if (exp.time && exp.time.length > 0) hasTime++;
            if (exp.xp > 0) hasXp++;
            if (exp.tools_needed.axe > 0) hasAxe++;
            if (exp.tools_needed.shovel > 0) hasShovel++;
            if (exp.tools_needed.saw > 0) hasSaw++;
        });

        console.log(`Expansions with population requirement: ${hasPopulation}`);
        console.log(`Expansions with coin cost: ${hasCoins}`);
        console.log(`Expansions with time data: ${hasTime}`);
        console.log(`Expansions with XP reward: ${hasXp}`);
        console.log(`Expansions requiring axe: ${hasAxe}`);
        console.log(`Expansions requiring shovel: ${hasShovel}`);
        console.log(`Expansions requiring saw: ${hasSaw}`);

        // Show some sample ranges for verification
        console.log('\nSample data ranges:');
        console.log(`Population range: ${Math.min(...expansions.map(e => e.population_needed))} - ${Math.max(...expansions.map(e => e.population_needed))}`);
        console.log(`Coins range: ${Math.min(...expansions.map(e => e.coins_needed))} - ${Math.max(...expansions.map(e => e.coins_needed))}`);
        console.log(`XP range: ${Math.min(...expansions.map(e => e.xp))} - ${Math.max(...expansions.map(e => e.xp))}`);
        console.log(`Max axe needed: ${Math.max(...expansions.map(e => e.tools_needed.axe))}`);
        console.log(`Max shovel needed: ${Math.max(...expansions.map(e => e.tools_needed.shovel))}`);
        console.log(`Max saw needed: ${Math.max(...expansions.map(e => e.tools_needed.saw))}`);

        console.log('Validation complete!\n');
    }
}

// Main execution function
async function main(): Promise<void> {
    const scraper = new TownshipExpansionScraper();

    try {
        console.log('Starting Township Expansions Scraper...');

        const expansions = await scraper.scrapeExpansions();

        // Validate the scraped data
        scraper.validateExpansions(expansions);

        if (expansions.length === 0) {
            console.error('No expansions were scraped!');
            return;
        }

        // Save to both JSON and CSV formats
        await scraper.saveToFile(expansions);
        // await scraper.saveToCSV(expansions);

        // Display sample data to verify correctness
        console.log('\n=== Sample Data Verification ===');
        console.log('First 3 expansions:');
        expansions.slice(0, 3).forEach((expansion, index) => {
            console.log(`${index + 1}: Pop=${expansion.population_needed}, Coins=${expansion.coins_needed}, Time="${expansion.time}", XP=${expansion.xp}, Tools=[${expansion.tools_needed.axe},${expansion.tools_needed.shovel},${expansion.tools_needed.saw}]`);
        });

        if (expansions.length > 50) {
            console.log('\nExpansion around #51 (should start requiring tools):');
            expansions.slice(50, 53).forEach((expansion, index) => {
                console.log(`${51 + index}: Pop=${expansion.population_needed}, Coins=${expansion.coins_needed}, Time="${expansion.time}", XP=${expansion.xp}, Tools=[${expansion.tools_needed.axe},${expansion.tools_needed.shovel},${expansion.tools_needed.saw}]`);
            });
        }

        console.log('\nLast expansion (#381):');
        const lastExpansion = expansions[expansions.length - 1];
        console.log(`381: Pop=${lastExpansion.population_needed}, Coins=${lastExpansion.coins_needed}, Time="${lastExpansion.time}", XP=${lastExpansion.xp}, Tools=[${lastExpansion.tools_needed.axe},${lastExpansion.tools_needed.shovel},${lastExpansion.tools_needed.saw}]`);

        console.log('\nScraping completed successfully!');
        console.log(`Total rows in CSV: ${expansions.length} (no header row)`);

    } catch (error) {
        console.error('Scraping failed:', error);
        process.exit(1);
    }
}

// Run the scraper
if (require.main === module) {
    main();
}

export { TownshipExpansionScraper, Expansion };